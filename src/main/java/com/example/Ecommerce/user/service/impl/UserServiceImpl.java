package com.example.Ecommerce.user.service.impl;

import static com.example.Ecommerce.security.jwt.JwtExpirationEnums.REFRESH_TOKEN_EXPIRATION_TIME;
import static com.example.Ecommerce.security.jwt.JwtTokenUtil.BEARER_PREFIX;

import com.example.Ecommerce.common.MailComponent;
import com.example.Ecommerce.config.CacheConfig;
import com.example.Ecommerce.coupon.domain.CouponType;
import com.example.Ecommerce.coupon.dto.CouponIssuanceDto;
import com.example.Ecommerce.coupon.service.CouponService;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.security.jwt.JwtTokenUtil;
import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.LogoutAccessToken;
import com.example.Ecommerce.user.domain.RefreshToken;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserAddressDto.Response;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.repository.DeliveryAddressRepository;
import com.example.Ecommerce.user.repository.LogoutAccessTokenRedisRepository;
import com.example.Ecommerce.user.repository.RefreshTokenRedisRepository;
import com.example.Ecommerce.user.repository.UserRepository;
import com.example.Ecommerce.user.service.UserService;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RefreshTokenRedisRepository refreshTokenRedisRepository;
  private final LogoutAccessTokenRedisRepository logoutAccessTokenRedisRepository;
  private final DeliveryAddressRepository deliveryAddressRepository;
  private final JwtTokenUtil jwtTokenUtil;
  private final MailComponent mailComponent;
  private final CouponService couponService;

  @Override
  public UserRegisterDto.Response registerUser(UserRegisterDto.Request request) {
    registerUserDuplicateCheck(request);

    String encryptedPassword = passwordEncoder.encode(request.getPassword());
    User user = userRepository.save(request.toEntity(encryptedPassword));

    CouponIssuanceDto.Request couponRequest = new CouponIssuanceDto.Request(user.getId(),
        CouponType.MEMBERSHIP_SIGNUP_COUPON, null);
    couponService.issuanceCoupon(couponRequest);

    mailComponent.sendVerifyLink(user.getId(), user.getEmail(), user.getName());

    return new UserRegisterDto.Response(user.getUserId());
  }

  @Override
  @Transactional
  public void verifyUserEmail(Long id) {
    User user = userRepository.findById(id)
        .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

    user.verifyUserEmail();
  }

  private void registerUserDuplicateCheck(UserRegisterDto.Request request) {
    if (userRepository.existsByUserId(request.getUserId())) {
      throw new CustomException(ErrorCode.USERID_ALREADY_EXISTS);
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
    }

    if (userRepository.existsByPhone(request.getPhone())) {
      throw new CustomException(ErrorCode.PHONE_ALREADY_EXISTS);
    }
  }

  @Override
  public UserLoginDto.Response login(UserLoginDto.Request request) {
    User user = userRepository.findByUserId(request.getUserId())
        .orElseThrow(() -> new NoSuchElementException("회원이 없습니다."));
    checkPassword(request.getPassword(), user.getPassword());

    String username = user.getUserId();
    String accessToken = jwtTokenUtil.generateAccessToken(username, user.getRole());
    RefreshToken refreshToken = saveRefreshToken(username);
    return UserLoginDto.Response.of(accessToken, refreshToken.getRefreshToken());
  }

  @Override
  public UserLoginDto.Response reissue(String refreshToken, String username) {
    refreshToken = resolveToken(refreshToken);
    if (refreshToken == null) {
      throw new CustomException(ErrorCode.REFRESH_TOKEN_NULL);
    }

    RefreshToken redisRefreshToken = refreshTokenRedisRepository.findById(username)
        .orElseThrow(NoSuchElementException::new);

    if (refreshToken.equals(redisRefreshToken.getRefreshToken())) {
      return reissueAccessToken(refreshToken, username);
    }
    throw new IllegalArgumentException("토큰이 일치하지 않습니다.");
  }

  @CacheEvict(value = CacheConfig.CacheKey.USER, key = "#username")
  public void logout(String accessToken, String username) {
    accessToken = resolveToken(accessToken);
    long remainMilliSeconds = jwtTokenUtil.getRemainMilliSeconds(accessToken);
    refreshTokenRedisRepository.deleteById(username);
    logoutAccessTokenRedisRepository.save(
        LogoutAccessToken.of(accessToken, username, remainMilliSeconds));
  }

  private void checkPassword(String rawPassword, String findMemberPassword) {
    if (!passwordEncoder.matches(rawPassword, findMemberPassword)) {
      throw new IllegalArgumentException("비밀번호가 맞지 않습니다.");
    }
  }

  private RefreshToken saveRefreshToken(String username) {
    return refreshTokenRedisRepository.save(RefreshToken.createRefreshToken(username,
        jwtTokenUtil.generateRefreshToken(username), REFRESH_TOKEN_EXPIRATION_TIME.getValue()));
  }

  private UserLoginDto.Response reissueAccessToken(String refreshToken, String username) {
    User user = userRepository.findByUserId(username)
        .orElseThrow(() -> new NoSuchElementException("토큰 재발급 오류 : 회원이 없습니다."));

    return new UserLoginDto.Response(jwtTokenUtil.generateAccessToken(username, user.getRole()),
        refreshToken); // 그게 아닌 경우 refresh 토큰은 재발급하지 않음
  }

  private String resolveToken(String token) {
    if (StringUtils.hasText(token) && token.startsWith(BEARER_PREFIX)) {
      return token.replace(BEARER_PREFIX, "");
    }
    return null;
  }

  @Override
  public void addUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails) {
    User user = userDetails.getUser();

    boolean existsRepresentAddress = deliveryAddressRepository.existsByUser(user);
    DeliveryAddress deliveryAddress = request.toEntity(user, existsRepresentAddress);

    deliveryAddressRepository.save(deliveryAddress);
  }

  @Override
  @Transactional
  public void modifyUserAddress(UserAddressDto.Request request, UserDetailsImpl userDetails,
      Long deliveryAddressId) {
    User user = userDetails.getUser();

    DeliveryAddress deliveryAddress = deliveryAddressRepository.findByUserAndId(user,
            deliveryAddressId)
        .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));

    deliveryAddress.modifyInfo(request);
  }

  @Override
  public void deleteUserAddress(UserDetailsImpl userDetails, Long deliveryAddressId) {
    User user = userDetails.getUser();

    DeliveryAddress deliveryAddress = deliveryAddressRepository.findByUserAndId(user,
            deliveryAddressId)
        .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));

    deliveryAddressRepository.delete(deliveryAddress);
  }

  @Override
  public List<UserAddressDto.Response> getUserAddressList(UserDetailsImpl userDetails,
      Pageable pageable) {
    User user = userDetails.getUser();

    Page<DeliveryAddress> deliveryAddressPage = deliveryAddressRepository.findAllByUser(user,
        pageable);
    return deliveryAddressPage.stream().map(Response::fromEntity)
        .collect(Collectors.toList());
  }

  @Override
  @Transactional
  public void setUserRepresentAddress(UserDetailsImpl userDetails, Long deliveryAddressId) {
    User user = userDetails.getUser();

    DeliveryAddress beforeRepresentAddress =
        deliveryAddressRepository.findByUserAndIsRepresentAddress(user, true)
            .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));
    beforeRepresentAddress.modifyRepresent();

    DeliveryAddress afterRepresentAddress =
        deliveryAddressRepository.findByUserAndId(user, deliveryAddressId)
            .orElseThrow(() -> new CustomException(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND));
    afterRepresentAddress.modifyRepresent();
  }
}

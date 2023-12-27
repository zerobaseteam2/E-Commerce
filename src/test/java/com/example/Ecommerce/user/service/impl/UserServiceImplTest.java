package com.example.Ecommerce.user.service.impl;

import static com.example.Ecommerce.user.domain.UserRole.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.Ecommerce.common.MailComponent;
import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.FindUserIdDto;
import com.example.Ecommerce.user.dto.FindUserPasswordDto;
import com.example.Ecommerce.user.dto.ResetPasswordDto;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserInfoDto;
import com.example.Ecommerce.user.repository.DeliveryAddressRepository;
import com.example.Ecommerce.user.repository.UserRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private DeliveryAddressRepository deliveryAddressRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @Mock
  private MailComponent mailComponent;

  @InjectMocks
  private UserServiceImpl userServiceImpl;

  @Test
  @DisplayName("배송지 추가 성공 테스트")
  void addUserAddressSuccess() {
    //given
    UserAddressDto.Request request = UserAddressDto.Request.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();

    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.of(2023, 12, 01))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    given(deliveryAddressRepository.existsByUser(any()))
        .willReturn(true);
    //when
    userServiceImpl.addUserAddress(request, userDetails);
    //then
    verify(deliveryAddressRepository).existsByUser(any());
    verify(deliveryAddressRepository).save(any());
  }

  @Test
  @DisplayName("배송지 수정 성공 테스트")
  void modifyUserAddressSuccess() {
    //given
    UserAddressDto.Request request = UserAddressDto.Request.builder()
        .roadAddress("서울특별시 샘플구 테스트로 2")
        .detailAddress("202동 202호")
        .zoneNo("12349")
        .addressName("집12")
        .phone("01012345679")
        .build();

    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.of(2023, 12, 01))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    DeliveryAddress beforeDeliveryAddress = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();

    given(deliveryAddressRepository.findByUserAndId(any(), anyLong()))
        .willReturn(Optional.of(beforeDeliveryAddress));
    //when
    userServiceImpl.modifyUserAddress(request, userDetails, 1L);
    //then
    verify(deliveryAddressRepository).findByUserAndId(any(), anyLong());
  }

  @Test
  @DisplayName("배송지 수정 실패 테스트")
  void modifyUserAddressNotFoundFail() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    given(deliveryAddressRepository.findByUserAndId(any(), anyLong()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userServiceImpl.deleteUserAddress(userDetails, 1L));
    //then
    assertEquals(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("배송지 삭제 성공 테스트")
  void deleteUserAddressSuccess() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    DeliveryAddress deliveryAddress = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();

    given(deliveryAddressRepository.findByUserAndId(any(), anyLong()))
        .willReturn(Optional.of(deliveryAddress));
    //when
    userServiceImpl.deleteUserAddress(userDetails, 1L);
    //then
    verify(deliveryAddressRepository).findByUserAndId(any(), anyLong());
    verify(deliveryAddressRepository).delete(any());
  }

  @Test
  @DisplayName("배송지 삭제 실패 테스트")
  void deleteUserAddressNotFoundFail() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    given(deliveryAddressRepository.findByUserAndId(any(), anyLong()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userServiceImpl.deleteUserAddress(userDetails, 1L));
    //then
    assertEquals(ErrorCode.DELIVERY_ADDRESS_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("배송지 조회 성공 테스트")
  void getUserAddressSuccess() {
    //given
    Pageable requestPageable = PageRequest.of(0, 10);

    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    DeliveryAddress deliveryAddress1 = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();
    DeliveryAddress deliveryAddress2 = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 2")
        .detailAddress("202동 202호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();

    List<DeliveryAddress> deliveryAddressList = new ArrayList<>();
    deliveryAddressList.add(deliveryAddress1);
    deliveryAddressList.add(deliveryAddress2);

    PageImpl<DeliveryAddress> deliveryAddressPage = new PageImpl<>(deliveryAddressList);

    given(deliveryAddressRepository.findAllByUser(any(), any()))
        .willReturn(deliveryAddressPage);
    //when
    List<UserAddressDto.Response> response = userServiceImpl.getUserAddressList(userDetails,
        requestPageable);
    //then
    assertEquals(response.get(0).getRoadAddress(), "서울특별시 샘플구 테스트로 1");
    assertEquals(response.get(1).getRoadAddress(), "서울특별시 샘플구 테스트로 2");

    verify(deliveryAddressRepository).findAllByUser(any(), any());
  }

  @Test
  @DisplayName("대표 배송지 설정 성공 테스트")
  void setUserRepresentAddressSuccess() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    DeliveryAddress deliveryAddress1 = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .isRepresentAddress(true)
        .build();
    DeliveryAddress deliveryAddress2 = DeliveryAddress.builder()
        .roadAddress("서울특별시 샘플구 테스트로 2")
        .detailAddress("202동 202호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .isRepresentAddress(false)
        .build();

    given(deliveryAddressRepository.findByUserAndIsRepresentAddress(any(), anyBoolean()))
        .willReturn(Optional.of(deliveryAddress1));
    given(deliveryAddressRepository.findByUserAndId(any(), anyLong()))
        .willReturn(Optional.of(deliveryAddress2));
    //when
    userServiceImpl.setUserRepresentAddress(userDetails, 1L);
    //then
    verify(deliveryAddressRepository).findByUserAndIsRepresentAddress(any(), anyBoolean());
    verify(deliveryAddressRepository).findByUserAndId(any(), any());
  }

  @Test
  @DisplayName("회원 정보 수정 성공 테스트")
  void modifyUserInfoSuccess() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();
    UserDetailsImpl userDetails = new UserDetailsImpl(user);

    UserInfoDto.Request request = UserInfoDto.Request.builder()
        .password("Test1234!@")
        .name("테스트2")
        .phone("01087654321")
        .birth(LocalDate.of(2023, 12, 15))
        .build();

    given(passwordEncoder.encode(anyString()))
        .willReturn("Test1234!@");

    ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
    //when
    userServiceImpl.modifyUserInfo(userDetails, request);
    //then
    verify(userRepository).save(captor.capture());
    assertEquals("Test1234!@", captor.getValue().getPassword());
    assertEquals("테스트2", captor.getValue().getName());
    assertEquals("01087654321", captor.getValue().getPhone());
    assertEquals(LocalDate.of(2023, 12, 15), captor.getValue().getBirth());
  }

  @Test
  @DisplayName("회원 아이디 찾기 성공 테스트")
  void findUserIdSuccess() {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();

    FindUserIdDto.Request request = FindUserIdDto.Request.builder()
        .email("Test@naver.com")
        .name("테스트")
        .build();

    given(userRepository.findByEmailAndName(anyString(), anyString()))
        .willReturn(Optional.of(user));
    //when
    userServiceImpl.findUserId(request);
    //then
    verify(mailComponent).sendUserId(anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("회원 아이디 찾기 실패 테스트")
  void findUserIdUserNotFoundFail() {
    //given
    FindUserIdDto.Request request = FindUserIdDto.Request.builder()
        .email("Test@naver.com")
        .name("테스트")
        .build();

    given(userRepository.findByEmailAndName(anyString(), anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userServiceImpl.findUserId(request));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원 비밀번호 찾기 메일 발송 성공 테스트")
  void sendToEmailResetPasswordFormSuccess() throws Exception {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();

    FindUserPasswordDto.Request request = FindUserPasswordDto.Request.builder()
        .email("Test@naver.com")
        .userId("Test")
        .build();

    given(userRepository.findByEmailAndUserId(anyString(), anyString()))
        .willReturn(Optional.of(user));
    //when
    userServiceImpl.sendToEmailResetPasswordForm(request);
    //then
    verify(mailComponent).sendResetPasswordForm(anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("회원 비밀번호 찾기 메일 발송 실패 테스트")
  void sendToEmailResetPasswordFormFail() {
    //given
    FindUserPasswordDto.Request request = FindUserPasswordDto.Request.builder()
        .email("Test@naver.com")
        .userId("Test")
        .build();

    given(userRepository.findByEmailAndUserId(anyString(), anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userServiceImpl.sendToEmailResetPasswordForm(request));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }

  @Test
  @DisplayName("회원 비밀번호 초기화 성공 테스트")
  void resetPasswordSuccess() throws Exception {
    //given
    User user = User.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.now().minusDays(1))
        .role(CUSTOMER)
        .emailVerify(true)
        .build();

    ResetPasswordDto.Request request = ResetPasswordDto.Request.builder()
        .newPassword("Test1234!@")
        .build();

    given(mailComponent.decryptUserId(anyString()))
        .willReturn("Test");
    given(passwordEncoder.encode(anyString()))
        .willReturn("Test1234!@");
    given(userRepository.findByUserId(anyString()))
        .willReturn(Optional.of(user));
    //when
    userServiceImpl.resetPassword(request, "decryptedUserId");
    //then
    verify(userRepository).findByUserId(any());
  }

  @Test
  @DisplayName("회원 비밀번호 초기화 실패 테스트")
  void resetPasswordUserNotFoundFail() throws Exception {
    //given
    ResetPasswordDto.Request request = ResetPasswordDto.Request.builder()
        .newPassword("Test1234!@")
        .build();

    given(mailComponent.decryptUserId(anyString()))
        .willReturn("Test");
    given(passwordEncoder.encode(anyString()))
        .willReturn("Test1234!@");
    given(userRepository.findByUserId(anyString()))
        .willReturn(Optional.empty());
    //when
    CustomException e = assertThrows(CustomException.class,
        () -> userServiceImpl.resetPassword(request, "decryptedUserId"));
    //then
    assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
  }
}
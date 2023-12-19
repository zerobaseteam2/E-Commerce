package com.example.Ecommerce.user.service.impl;

import static com.example.Ecommerce.user.domain.UserRole.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.Ecommerce.exception.CustomException;
import com.example.Ecommerce.exception.ErrorCode;
import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.repository.DeliveryAddressRepository;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

  @Mock
  private DeliveryAddressRepository deliveryAddressRepository;

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
    List<UserAddressDto.Response> response = userServiceImpl.getUserAddressList(userDetails, requestPageable);
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
}
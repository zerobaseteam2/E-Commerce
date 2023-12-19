package com.example.Ecommerce.user.service.impl;

import static com.example.Ecommerce.user.domain.UserRole.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import com.example.Ecommerce.security.UserDetailsImpl;
import com.example.Ecommerce.user.domain.DeliveryAddress;
import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.repository.DeliveryAddressRepository;
import com.example.Ecommerce.user.repository.UserRepository;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
        .birth(LocalDate.of(2023,12,01))
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
        .birth(LocalDate.of(2023,12,01))
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
}
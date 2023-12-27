package com.example.Ecommerce.user.controller;

import static com.example.Ecommerce.security.jwt.JwtTokenUtil.AUTHORIZATION_HEADER;
import static com.example.Ecommerce.security.jwt.JwtTokenUtil.BEARER_PREFIX;
import static com.example.Ecommerce.user.domain.UserRole.CUSTOMER;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Ecommerce.config.TestConfig;
import com.example.Ecommerce.user.dto.FindUserIdDto;
import com.example.Ecommerce.user.dto.FindUserPasswordDto;
import com.example.Ecommerce.user.dto.ResetPasswordDto;
import com.example.Ecommerce.user.dto.UserAddressDto;
import com.example.Ecommerce.user.dto.UserInfoDto;
import com.example.Ecommerce.user.dto.UserLoginDto;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@Import(TestConfig.class)
@WebMvcTest(UserController.class)
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private UserService userService;


  @Test
  @DisplayName("회원 가입 성공 테스트")
  void registerUserSuccess() throws Exception {
    //given
    given(userService.registerUser(any()))
        .willReturn(UserRegisterDto.Response.builder()
            .userId("Test")
            .build());

    UserRegisterDto.Request request = UserRegisterDto.Request.builder()
        .userId("Test")
        .password("Test1234!")
        .name("테스트")
        .email("Test@naver.com")
        .phone("01012345678")
        .birth(LocalDate.of(2023, 12, 12))
        .role(CUSTOMER)
        .build();
    //when
    //then
    mockMvc.perform(
            post("/api/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.userId").exists())
        .andDo(print());

    verify(userService).registerUser(any());
  }

  @Test
  @DisplayName("로그인 성공 테스트")
  void loginUserSuccess() throws Exception {
    //given

    UserLoginDto.Request request = UserLoginDto.Request.builder()
        .userId("Test")
        .password("Test1234!")
        .build();

    given(userService.login(any(UserLoginDto.Request.class)))
        .willReturn(UserLoginDto.Response.of(
            BEARER_PREFIX + "accessToken",
            BEARER_PREFIX + "refreshToken"));

    //when

    //then
    MvcResult result = mockMvc.perform(post("/api/user/login")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andReturn();
    String token = result.getResponse().getHeader(AUTHORIZATION_HEADER);

    assertNotNull(token);
    assertTrue(token.startsWith("Bearer "));
  }

  @Test
  @DisplayName("배송지 추가 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void registerUserAddressSuccess() throws Exception {
    //given
    UserAddressDto.Request request = UserAddressDto.Request.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();
    //when
    //then
    mockMvc.perform(
            post("/api/user/address")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).addUserAddress(any(), any());
  }

  @Test
  @DisplayName("배송지 수정 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void modifyUserAddressSuccess() throws Exception {
    //given
    UserAddressDto.Request request = UserAddressDto.Request.builder()
        .roadAddress("서울특별시 샘플구 테스트로 1")
        .detailAddress("101동 101호")
        .zoneNo("12345")
        .addressName("집")
        .phone("01012345678")
        .build();
    //when
    //then
    mockMvc.perform(
            put("/api/user/address/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).modifyUserAddress(any(), any(), anyLong());
  }

  @Test
  @DisplayName("배송지 삭제 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void deleteUserAddressSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            delete("/api/user/address/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).deleteUserAddress(any(), anyLong());
  }

  @Test
  @DisplayName("배송지 조회 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void getUserAddressSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            get("/api/user/address")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).getUserAddressList(any(), any());
  }

  @Test
  @DisplayName("대표 배송지 설정 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void setUserRepresentAddressSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            put("/api/user/address/represent/1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).setUserRepresentAddress(any(), any());
  }

  @Test
  @DisplayName("회원 정보 수정 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void modifyUserInfoSuccess() throws Exception {
    //given
    UserInfoDto.Request request = UserInfoDto.Request.builder()
        .password("Test1234!@")
        .name("테스트2")
        .phone("01087654321")
        .birth(LocalDate.of(2023, 12, 15))
        .build();
    //when
    //then
    mockMvc.perform(
            put("/api/user/info")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).modifyUserInfo(any(), any());
  }

  @Test
  @DisplayName("회원 아이디 찾기 성공 테스트")
  void findUserIdSuccess() throws Exception {
    //given
    FindUserIdDto.Request request = FindUserIdDto.Request.builder()
        .email("Test@naver.com")
        .name("테스트")
        .build();
    //when
    //then
    mockMvc.perform(
            get("/api/user/find/userId")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).findUserId(any());
  }

  @Test
  @DisplayName("회원 비밀번호 찾기 메일 발송 성공 테스트")
  void sendToEmailResetPasswordFormSuccess() throws Exception {
    //given
    FindUserPasswordDto.Request request = FindUserPasswordDto.Request.builder()
        .email("Test@naver.com")
        .userId("Test")
        .build();
    //when
    //then
    mockMvc.perform(
            get("/api/user/reset/password")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).sendToEmailResetPasswordForm(any());
  }

  @Test
  @DisplayName("회원 비밀번호 초기화 성공 테스트")
  void resetPasswordSuccess() throws Exception {
    //given
    ResetPasswordDto.Request request = ResetPasswordDto.Request.builder()
        .newPassword("Test1234!@")
        .build();
    //when
    //then
    mockMvc.perform(
            put("/api/user/reset/password/Test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).resetPassword(any(), anyString());
  }

  @Test
  @DisplayName("회원 탈퇴 성공 테스트")
  @WithMockUser(authorities = {"ROLE_CUSTOMER"})
  void unregisterUserSuccess() throws Exception {
    //given
    //when
    //then
    mockMvc.perform(
            delete("/api/user/unregister")
                .header("Authorization", "accessToken"))
        .andExpect(status().isOk())
        .andDo(print());

    verify(userService).unregisterUser(anyString(), any());
  }
}

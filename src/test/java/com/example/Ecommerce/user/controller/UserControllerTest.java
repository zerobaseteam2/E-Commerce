package com.example.Ecommerce.user.controller;

import com.example.Ecommerce.TestConfig;
import com.example.Ecommerce.user.dto.UserRegisterDto;
import com.example.Ecommerce.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static com.example.Ecommerce.user.domain.UserRole.CUSTOMER;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
            .birth(new Date())
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
  
}

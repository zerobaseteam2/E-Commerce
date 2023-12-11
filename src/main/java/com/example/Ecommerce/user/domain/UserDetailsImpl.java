package com.example.Ecommerce.user.domain;

import com.example.Ecommerce.user.domain.User;
import com.example.Ecommerce.user.domain.UserRole;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
@RequiredArgsConstructor
public class UserDetailsImpl implements UserDetails {
  private final User user;
  
  public User getUser() {
    return user;
  }
  
  @Override
  public String getPassword() {
    return user.getPassword();
  }
  
  @Override
  public String getUsername() {
    return user.getUserId();
  }
  
  
  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {    // authorities를 통해 권한 제어 가능. GrantedAuthority : 현재 사용자가 가지고 있는 권한
    UserRole userRole = user.getRole();
    String authority = userRole.getAuthority();
    
    SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(authority);
    Collection<GrantedAuthority> authorities = new ArrayList<>();
    authorities.add(simpleGrantedAuthority);
    
    return authorities;
  }
  
  @Override
  public boolean isAccountNonExpired() {
    return false;
  }
  
  @Override
  public boolean isAccountNonLocked() {
    return false;
  }
  
  @Override
  public boolean isCredentialsNonExpired() {
    return false;
  }
  
  @Override
  public boolean isEnabled() {
    return false;
  }
}

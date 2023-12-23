package com.example.Ecommerce.user.repository;

import com.example.Ecommerce.user.domain.LogoutAccessToken;
import org.springframework.data.repository.CrudRepository;

public interface LogoutAccessTokenRedisRepository extends
    CrudRepository<LogoutAccessToken, String> {

}

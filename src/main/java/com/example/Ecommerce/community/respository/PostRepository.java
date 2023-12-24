package com.example.Ecommerce.community.respository;

import com.example.Ecommerce.community.domain.Post;
import com.example.Ecommerce.order.domain.Order;
import com.example.Ecommerce.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}

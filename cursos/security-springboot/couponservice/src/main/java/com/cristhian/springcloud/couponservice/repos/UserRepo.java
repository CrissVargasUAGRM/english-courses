package com.cristhian.springcloud.couponservice.repos;

import com.cristhian.springcloud.couponservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

package com.cristhian.product.productapi.repos;

import com.cristhian.product.productapi.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmail(String email);
}

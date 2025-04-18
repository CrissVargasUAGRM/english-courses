package com.cristhian.product.productapi.repos;

import com.cristhian.product.productapi.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
}

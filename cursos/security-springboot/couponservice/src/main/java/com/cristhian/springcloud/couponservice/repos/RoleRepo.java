package com.cristhian.springcloud.couponservice.repos;

import com.cristhian.springcloud.couponservice.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
}

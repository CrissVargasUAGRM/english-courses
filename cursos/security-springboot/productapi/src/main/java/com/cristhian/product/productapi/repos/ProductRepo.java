package com.cristhian.product.productapi.repos;

import com.cristhian.product.productapi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepo extends JpaRepository<Product, Long> {

}

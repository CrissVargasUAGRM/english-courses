package com.cristhian.product.productapi.controller;

import com.cristhian.product.productapi.dto.Coupon;
import com.cristhian.product.productapi.model.Product;
import com.cristhian.product.productapi.repos.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@RestController
@RequestMapping("/productapi")
public class ProductController {
    @Autowired
    private ProductRepo repo;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping("/product")
    public Product create(@RequestBody Product product){
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("john@ferguson.com", "john"));
        Coupon coupon = restTemplate.getForObject("http://localhost:8082/couponapi/coupon/"+product.getCouponCode(), Coupon.class);
        product.setPrice(product.getPrice().subtract(coupon.getDiscount()));
        return repo.save(product);
    }

    @GetMapping("/product/{id}")
    public Optional<Product> getProduct(@PathVariable("id") Long id){
        return repo.findById(id);
    }
}

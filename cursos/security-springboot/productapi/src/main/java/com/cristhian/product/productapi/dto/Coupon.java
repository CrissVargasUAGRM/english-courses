package com.cristhian.product.productapi.dto;

import jakarta.persistence.Entity;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Coupon {
    private Long id;

    private String code;

    private BigDecimal discount;

    private String exp_date;
}

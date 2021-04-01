package com.example.jpa.pay.model;

import com.example.jpa.pay.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ProductResponse {

    private long totalInvestingAmount;

    private String title;

    public static ProductResponse of(Product product) {
        return ProductResponse.builder ()
                .totalInvestingAmount(product.getTotalInvestingAmount ())
                .title(product.getTitle ())
                .build ();
    }
}

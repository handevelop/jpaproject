package com.example.jpa.pay.model;

import com.example.jpa.pay.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class UserInvestInput {

    private Long investAmount;

    private Long id;

}

package com.example.jpa.pay.repository;


import com.example.jpa.pay.entity.PayUser;
import com.example.jpa.pay.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
// 상속을 받을때 모델 타입과 모델의 PK 를 상속 받는다.
public interface ProductrRepository extends JpaRepository<Product, Long> {

}

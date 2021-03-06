package com.atguigu.springcloud.service;

import com.atguigu.springcloud.entites.Payment;
import org.apache.ibatis.annotations.Param;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-09 10:10
 */
public interface PaymentService {
    // 写
    public int create(Payment payment);

    // 读
    public  Payment getPaymentById(@Param("id") Long id);

    // 改
    public void updatePaymentById(Payment payment);

    // 删
    public void deleteById(@Param("id") Long id);
}

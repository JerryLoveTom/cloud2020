package com.atguigu.springcloud.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @program: cloud2020
 * @description:
 * @author: PP Zhang
 * @create: 2020-06-09 15:25
 */
@Configuration
public class ApplicationContextConfig {
    // 这里的操作相当于applicationContext.xml中的<bean id="" class="">
    @Bean
   // @LoadBalanced // 开启负载均衡  // 自定义时，需要取消该注解
    public RestTemplate getRestTemplate(){
        return  new RestTemplate();
    }
}

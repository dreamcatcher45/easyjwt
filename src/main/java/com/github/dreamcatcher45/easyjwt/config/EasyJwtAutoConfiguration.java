package com.github.dreamcatcher45.easyjwt.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
@ComponentScan("com.github.dreamcatcher45.easyjwt")
public class EasyJwtAutoConfiguration {}
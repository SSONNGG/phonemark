package com.gsn.pm;

import com.gsn.pm.filters.AuthorizedRequestFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableZuulProxy
@SpringCloudApplication
public class ZuulApp {

    public static void main(String[] args) {
        SpringApplication.run(ZuulApp.class,args);
    }
    @Bean
    public AuthorizedRequestFilter getAuthorizedRequestFilter() {
        return new AuthorizedRequestFilter();
    }
}
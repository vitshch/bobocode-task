package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.annotation.Bean;

@Bean
public class GreetingService {

    public void greeting() {
        System.out.println("Hello!!!");
    }

}

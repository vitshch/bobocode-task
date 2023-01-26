package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.annotation.Bean;

@Bean("userDetailsService")
public class UserService {

    public User getUser() {
        return new User("Vitalii", "Shcherban");
    }

}

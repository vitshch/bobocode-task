package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.annotation.Autowired;
import com.bobocode.vitalish.context.annotation.Bean;

@Bean
public class GreetingService {

    @Autowired
    private UserService userService;

    @Autowired
    private WeekdayService weekdayService;

    public void greeting() {
        User user = userService.getUser();
        System.out.println("Hello " + user.firstName() + " " + user.lastName());
        System.out.println("Today is: " + weekdayService.getDayOfTheWeek());
    }

}

package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.AnnotationConfigApplicationContext;
import com.bobocode.vitalish.context.ApplicationContext;

import java.util.Map;

public class Application {

    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext("com.bobocode.vitalish.context.demo");

            GreetingService greetingService = context.getBean(GreetingService.class);
            greetingService.greeting();

            UserService userService = context.getBean(UserService.class);
            System.out.println(userService.getUser());

            Map<String, UserService> allBeans = context.getAllBeans(UserService.class);
            allBeans.entrySet().forEach(System.out::println);

            System.out.println(context.getBean("userDetailsService", UserService.class));
            System.out.println(context.getBean("userService", UserService.class));

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}

package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.AnnotationConfigApplicationContext;
import com.bobocode.vitalish.context.ApplicationContext;

public class Application {

    public static void main(String[] args) {
        try {
            ApplicationContext context = new AnnotationConfigApplicationContext("com.bobocode.vitalish.context.demo");

            GreetingService greetingService = context.getBean(GreetingService.class);
            greetingService.greeting();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}

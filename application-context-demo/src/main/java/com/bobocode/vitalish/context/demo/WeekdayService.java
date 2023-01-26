package com.bobocode.vitalish.context.demo;

import com.bobocode.vitalish.context.annotation.Bean;

import java.time.LocalDateTime;
import java.time.format.TextStyle;
import java.util.Locale;

@Bean
public class WeekdayService {

    public String getDayOfTheWeek() {
        return LocalDateTime.now().getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
    }

}

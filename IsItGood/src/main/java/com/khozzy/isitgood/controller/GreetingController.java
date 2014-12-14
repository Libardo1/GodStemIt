package com.khozzy.isitgood.controller;

import com.khozzy.isitgood.domain.User;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    public static final String template = "Hello %s, you're in restricted area.";

    private final AtomicLong counter = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting greeting(@AuthenticationPrincipal User user) {
        return new Greeting(counter.incrementAndGet(), String.format(template, user.getName()));
    }
}

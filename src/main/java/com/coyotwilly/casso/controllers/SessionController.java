package com.coyotwilly.casso.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
@RequiredArgsConstructor
public class SessionController {
    public static class SessionControllerPath {
        public static final String SESSIONS = "/session";
    }
}

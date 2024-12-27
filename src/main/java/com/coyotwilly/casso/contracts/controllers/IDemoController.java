package com.coyotwilly.casso.contracts.controllers;

import com.coyotwilly.casso.dtos.CredentialDto;
import com.coyotwilly.casso.dtos.FullLogoutDto;
import com.coyotwilly.casso.dtos.ValidationResult;
import com.coyotwilly.casso.exceptions.CredentialTypeException;
import com.coyotwilly.casso.models.entities.Session;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface IDemoController {
    @PostMapping("/login")
    ResponseEntity<Session> login(@RequestBody CredentialDto credential) throws Exception;

    @GetMapping("/validate/{type}/{id}")
    ResponseEntity<ValidationResult> validate(@PathVariable String type, @PathVariable String id)
            throws CredentialTypeException;

    @PostMapping("/logout/{type}/{id}")
    ResponseEntity<String> logout(@PathVariable String type, @PathVariable String id)
            throws CredentialTypeException;

    @PostMapping("/logout")
    ResponseEntity<String> logout(@RequestBody FullLogoutDto dto);
}

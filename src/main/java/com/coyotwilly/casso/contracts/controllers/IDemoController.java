package com.coyotwilly.casso.contracts.controllers;

import com.coyotwilly.casso.dtos.CredentialDto;
import com.coyotwilly.casso.dtos.FullLogoutDto;
import com.coyotwilly.casso.dtos.ValidationResult;
import com.coyotwilly.casso.exceptions.CredentialTypeException;
import com.coyotwilly.casso.models.entities.Session;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Base Operations API", description = "API most common action like login, logout and session validity check")
public interface IDemoController {
    @Operation(
            summary = "Challenge user login credentials",
            description = "Check are passed credentials valid and is user free to enter the application -" +
                    " no active locks because of brute force detection")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully logged in"),
            @ApiResponse(responseCode = "400", description = "Malformed request passed"),
            @ApiResponse(responseCode = "403", description = "Invalid credentials passed or brute force lock is active"),
    })
    @PostMapping("/login")
    ResponseEntity<Session> login(@RequestBody CredentialDto credential) throws Exception;

    @Operation(
            summary = "Allows to check is user session still valid for given auth type",
            description = "Checks is user session still available in Cassandra and then validates its" +
                    " properties in order to get validation result")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns validation result"),
            @ApiResponse(responseCode = "400", description = "Invalid auth type or identifier"),
            @ApiResponse(responseCode = "500", description = "Database operation unexpectedly failed"),
    })
    @GetMapping("/validate/{type}/{id}")
    ResponseEntity<ValidationResult> validate(@PathVariable String type, @PathVariable String id)
            throws CredentialTypeException;

    @Operation(
            summary = "Single user session logout - only this device",
            description = "Allows to destroy session by passed identifier only for given device. Other devices related " +
                    "with given user will still be logged in and able to work")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful device logout"),
            @ApiResponse(responseCode = "400", description = "Invalid auth type or identifier"),
            @ApiResponse(responseCode = "404", description = "Passed session does not exist in Cassandra"),
            @ApiResponse(responseCode = "500", description = "Database operation unexpectedly failed"),
    })
    @PostMapping("/logout/{type}/{id}")
    ResponseEntity<String> logout(@PathVariable String type, @PathVariable String id)
            throws CredentialTypeException;

    @Operation(
            summary = "User logout - all devices",
            description = "Allows to destroy all sessions related with given user login. Which results in sudden delete from all other devices")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful device logout"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "404", description = "Passed session does not exist or has been deleted"),
            @ApiResponse(responseCode = "500", description = "Database operation unexpectedly failed"),
    })
    @PostMapping("/logout")
    ResponseEntity<String> logout(@RequestBody FullLogoutDto dto);
}

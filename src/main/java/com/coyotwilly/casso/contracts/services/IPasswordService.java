package com.coyotwilly.casso.contracts.services;

public interface IPasswordService {
    String encryptPassword(String password);
    Boolean validatePassword(String password, String encryptedPassword);
}

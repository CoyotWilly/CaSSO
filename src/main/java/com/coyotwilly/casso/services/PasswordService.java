package com.coyotwilly.casso.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Service
public class PasswordService {
    @Value("${casso.password.algorithm:HmacSHA256}")
    private String algorithm;

    @Value("${casso.password.key:123465789}")
    private String key;

    public String encryptPassword(String password) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(), algorithm);
            Mac mac = Mac.getInstance(algorithm);
            mac.init(secretKeySpec);

            return bytesToHex(mac.doFinal(password.getBytes()));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            log.error("Invalid configuration provided", e);

            return password;
        }
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte h : hash) {
            String hex = Integer.toHexString(0xff & h);
            if (hex.length() == 1) {
                hexString.append('0');
            }

            hexString.append(hex);
        }

        return hexString.toString();
    }
}

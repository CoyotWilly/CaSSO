package com.coyotwilly.casso.utils;

import com.coyotwilly.casso.models.entities.User;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Slf4j
public final class CsvParserUtil {
    public static List<User> parseCredentials() {
        List<User> users = new ArrayList<>();
        try (InputStream inputStream = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("users.csv")) {
            if (inputStream == null) {
                return users;
            }

            Scanner scanner = new Scanner(inputStream);
            scanner.nextLine();

            while (scanner.hasNextLine()) {
                String[] field = scanner.nextLine().split(";");
                if (field.length < 1) {
                    continue;
                }

                users.add(
                        User.builder()
                                .login(field[0])
                                .build()
                );
            }
        }
        catch (IOException e) {
            log.warn("Data import failed with following {}", e.getMessage());
        }

        return users;
    }
}

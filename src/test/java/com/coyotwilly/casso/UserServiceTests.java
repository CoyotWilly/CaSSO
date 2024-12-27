package com.coyotwilly.casso;

import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import com.coyotwilly.casso.utils.CsvParserUtil;
import com.coyotwilly.casso.utils.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserServiceTests {
    @Autowired
    private IUserService userService;

    private final int threadCount = 1000;
    private final int operationCount = 10;

    private List<User> users;

    @BeforeAll
    public void setUp() {
        users = CsvParserUtil.parseCredentials();

        if (users.isEmpty()) {
            Assertions.fail("No users found");
        }
    }

    @Test
    public void should_insert_user_credentials() {
        List<Task> tasks = new ArrayList<>();

        for (int i = 0; i < threadCount; i++) {
            tasks.add(new Task(i * operationCount, (i + 1) * operationCount, userService, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(threadCount)) {
            executorService.invokeAll(tasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, operationCount);
    }
}

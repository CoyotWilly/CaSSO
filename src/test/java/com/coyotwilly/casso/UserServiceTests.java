package com.coyotwilly.casso;

import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import com.coyotwilly.casso.tasks.UserMockupTask;
import com.coyotwilly.casso.utils.CsvParserUtil;
import com.coyotwilly.casso.utils.ThreadingConstants;
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
        List<UserMockupTask> userMockupTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            userMockupTasks.add(new UserMockupTask(i * ThreadingConstants.OperationCount,
                    (i + 1) * ThreadingConstants.OperationCount,
                    userService, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(userMockupTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }
}

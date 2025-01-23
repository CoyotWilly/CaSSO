package com.coyotwilly.casso;

import com.coyotwilly.casso.contracts.controllers.IDemoController;
import com.coyotwilly.casso.contracts.services.IUserService;
import com.coyotwilly.casso.models.entities.User;
import com.coyotwilly.casso.tasks.LoginBenchmarkTask;
import com.coyotwilly.casso.tasks.LogoutBenchmarkTask;
import com.coyotwilly.casso.tasks.SessionValidationTask;
import com.coyotwilly.casso.utils.ThreadingConstants;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class BenchmarkTests {
    @Autowired
    private IDemoController controller;

    @Autowired
    private IUserService userService;

    private List<User> users;

    @BeforeAll
    public void setup() {
        users = userService.getUsers();

        if (users.isEmpty()) {
            Assertions.fail("No users found");
        }
    }

    @AfterAll
    public void cleanup() {
        if (!users.isEmpty()) {
            users = null;
        }
    }

    @Test
    public void performanceSameUsersLoginTest() {
        List<LoginBenchmarkTask> loginBenchmarkTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            loginBenchmarkTasks.add(new LoginBenchmarkTask(i * ThreadingConstants.OperationCount,
                    i * ThreadingConstants.OperationCount,
                    controller, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(loginBenchmarkTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }

    @Test
    public void performanceDifferentUsersLoginTest() {
        List<LoginBenchmarkTask> loginBenchmarkTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            loginBenchmarkTasks.add(new LoginBenchmarkTask(i * ThreadingConstants.OperationCount,
                    (i + 1) * ThreadingConstants.OperationCount,
                    controller, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(loginBenchmarkTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }

    @Test
    public void performanceSameUsersSessionValidationTest() {
        List<SessionValidationTask> sessionValidationTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            sessionValidationTasks.add(new SessionValidationTask(i * ThreadingConstants.OperationCount,
                    i * ThreadingConstants.OperationCount,
                    controller, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(sessionValidationTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }

    @Test
    public void performanceDifferentUsersSessionValidationTest() {
        List<SessionValidationTask> sessionValidationTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            sessionValidationTasks.add(new SessionValidationTask(i * ThreadingConstants.OperationCount,
                    (i + 1) * ThreadingConstants.OperationCount,
                    controller, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(sessionValidationTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }

    @Test
    public void performanceUsersLogoutTest() {
        List<LogoutBenchmarkTask> logoutBenchmarkTasks = new ArrayList<>();

        for (int i = 0; i < ThreadingConstants.ThreadCount; i++) {
            logoutBenchmarkTasks.add(new LogoutBenchmarkTask(i * ThreadingConstants.OperationCount,
                    (i + 1) * ThreadingConstants.OperationCount,
                    controller, users));
        }

        try (ExecutorService executorService = Executors.newFixedThreadPool(ThreadingConstants.ThreadCount)) {
            executorService.invokeAll(logoutBenchmarkTasks);
        } catch (InterruptedException e) {
            Assertions.fail(e.getMessage());
        }

        Assertions.assertEquals(100, ThreadingConstants.ThreadCount);
    }
}

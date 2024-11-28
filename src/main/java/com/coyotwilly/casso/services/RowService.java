package com.coyotwilly.casso.services;

import com.datastax.oss.driver.api.core.CqlSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RowService {
    private final CqlSession cql;

    public void insert() {
        cql.execute("insert into emp(emp_id, emp_city, emp_name)\n" +
                        "values (10,'name', 'city')");
    }

    public void update() {
        cql.execute(
                "UPDATE emp SET emp_name = ? WHERE emp_id = ?",
                "Leonor", 10);
    }

    public void delete() {
        cql.execute(
                "DELETE FROM casso.emp WHERE emp_id = ?", 10);
    }
}

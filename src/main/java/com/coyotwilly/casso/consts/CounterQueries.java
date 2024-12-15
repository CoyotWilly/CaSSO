package com.coyotwilly.casso.consts;

public final class CounterQueries {
    public static String GET_COUNTER_VALUE = "SELECT counter FROM %s WHERE %s = ?";
    public static String CREATE_COUNTER_QUERY = "UPDATE %1$s SET %2$s = %2$s + ? WHERE %3$s = ?";
    public static String INCREMENT_COUNTER_QUERY = "UPDATE %1$s SET %2$s = %2$s + ? WHERE %3$s = ?";
    public static String DECREMENT_COUNTER_QUERY = "UPDATE %1$s SET %2$s = %2$s - ? WHERE %3$s = ?";
}

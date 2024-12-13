package com.coyotwilly.casso.consts;

public class UserQueries {
    public static final String SELECT_ALL_USERS = "select * from users";
    public static final String SELECT_USER_BY_ID = "select * from %1$s where %2$s = ?";
    public static final String DELETE_USER_BY_ID = "delete from %1$s where %2$s = ? IF EXISTS";
}

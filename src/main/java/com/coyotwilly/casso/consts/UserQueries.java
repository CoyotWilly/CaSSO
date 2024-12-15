package com.coyotwilly.casso.consts;

public final class UserQueries {
    public static final String SELECT_ALL_USERS = "select * from users";
    public static final String SELECT_USER_BY_ID = "select * from %1$s where %2$s = ?";
}

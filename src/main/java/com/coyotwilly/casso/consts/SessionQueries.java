package com.coyotwilly.casso.consts;

public final class SessionQueries {
    public static final String SELECT_ALL_SESSIONS = "select * from sessions";
    public static final String SELECT_SESSION_BY_EMAIL = "select * from %1$s where %2$s = ?";
    public static final String SELECT_SESSION_BY_ID = "select * from %1$s where %2$s = ?";
}

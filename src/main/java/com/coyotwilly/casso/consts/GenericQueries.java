package com.coyotwilly.casso.consts;

public class GenericQueries {
    public static final String GENERIC_INSERT = "insert into %1$s (%2$s) values (%3$s)";
    public static final String GENERIC_UPDATE = "update %1$s set %2$s = ? where %3$s = ? IF EXISTS";
}

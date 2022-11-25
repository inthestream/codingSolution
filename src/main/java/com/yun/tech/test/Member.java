package com.yun.tech.test;

import java.io.Serializable;

public class Member implements Serializable {
    private static final long serialVersionID = 1L;

    private String name;
    private String email;
    private int age;


    public Member(String name, String email, int age) {
        this.name = name;
        this.email = email;
        this.age = age;

    }

    @Override
    public String toString() {
        return String.format("Member{name='%s', email='%s', age='%s',", name, email, age);
    }
}
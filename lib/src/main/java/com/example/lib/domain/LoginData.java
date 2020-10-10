package com.example.lib.domain;

public class LoginData {
    public LoginResponse data;
    public int result;
    public static class LoginResponse{
        public String JSESSIONID;
        public String user;
        public String userName;
        public String roleID;
    }
}

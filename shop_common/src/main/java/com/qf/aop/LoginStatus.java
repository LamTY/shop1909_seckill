package com.qf.aop;

import com.qf.entity.User;

public class LoginStatus {

    private static ThreadLocal<User> userThreadLocal = new ThreadLocal<>();

    public static boolean islogin(){
      return userThreadLocal.get() != null;
    }

    public static void setUser(User user){
        LoginStatus.userThreadLocal.set(user);
    }

    public static User getUser(){
        return userThreadLocal.get();
    }
}



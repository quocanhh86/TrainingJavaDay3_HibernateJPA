package com.example.springhibernatedatajpa.infrastructure.constant;

public class PathAuthenticationConstant {
    public static final String LOGIN_PREFIX = PathConstant.DEFAULT_PATH + "/login";
    public static final String LOGOUT_PREFIX = PathConstant.DEFAULT_PATH + "/logout";
    public static final String REGISTER_PREFIX = PathConstant.DEFAULT_PATH + "/register";
    public static final String SEND_OTP_PREFIX = PathConstant.DEFAULT_PATH + "/send-otp";
    public static final String VERIFY_OTP_PREFIX = PathConstant.DEFAULT_PATH + "/verify-otp";

    public static final String ERROR_401 =  PathConstant.DEFAULT_PATH + "/401";
    public static final String ERROR_403 =  PathConstant.DEFAULT_PATH + "/403";
    public static final String ERROR_500 =  PathConstant.DEFAULT_PATH + "/500";
    public static final String MAINTAIN_PREFIX = PathConstant.DEFAULT_PATH + "/maintain";
}

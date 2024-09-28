package com.OtpApp.OtpApplication.Constraints;

public class OtpAppConstraints {

    public static final String SALT = "SaltKeyUsedInCreationOfSecretUsingPassword";
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String HMACALGORITHM = "HmacSha1";
    public static final String FAIL = "Failed";
    public static final String DELIMITER = "$";
    public static final String RANDOMSTR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final int SECRETSIZE = 16;
    public static final String SUCCESS = "Success";
    public static final int OTPVALIDITY = 60;
    public static final boolean TRUE = true;
    public static final boolean FALSE = false;
    public static final int FETCH_INTERVAL = 10;
    public static final int DURATION = 10;
    public static final String DEFAULTOTP = "000000";
    public static final String ACTIVATE = "activate";
    public static final String VALIDATE = "validate";
}

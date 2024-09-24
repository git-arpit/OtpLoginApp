package com.OtpApp.OtpApplication.Constraints;

public class OtpAppConstraints {

    public static final String SALT = "SaltKeyUsedInCreationOfSecretUsingPassword";
    public static final String ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final String FAIL = "FAILED";

    public static final String DELIMITER = "$";
    public static final String RANDOMSTR = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    public static final int SECRETSIZE = 16 ;
    public static final CharSequence REACTIVATE = "reactivate";
    public static final CharSequence ACTIVATE = "activate";
}

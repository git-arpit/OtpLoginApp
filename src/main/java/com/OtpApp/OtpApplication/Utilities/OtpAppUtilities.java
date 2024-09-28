package com.OtpApp.OtpApplication.Utilities;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.*;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class OtpAppUtilities {
    @Autowired
    CustomMsg customMsg;

    IvParameterSpec spec = EncryptionUtil.generateIv();

    public String decoder(String encodedString) {
        String decodedPass = new String(Base64.getDecoder().decode(encodedString));
        return decodedPass;
    }

    public String encryptSecret(String password, String userSecret) {

        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(password, OtpAppConstraints.SALT);
        return EncryptionUtil.encrypt(OtpAppConstraints.ALGORITHM, userSecret, secretKey, spec);
    }

    public String decryptSecret(String cipher, String decodedPass) {
        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(decodedPass, OtpAppConstraints.SALT);
        System.out.println("Cipher : " + cipher);
        try {
            String decrypt = EncryptionUtil.decrypt(OtpAppConstraints.ALGORITHM, cipher, secretKey, spec);
            return decrypt;
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public String generateSecret(EncryptUserDto encryptUserDto) {
        String finalRandomString = OtpAppConstraints.RANDOMSTR.concat(encryptUserDto.getUserID()).concat(encryptUserDto.getUserPass());
        StringBuffer userSecret = new StringBuffer(OtpAppConstraints.SECRETSIZE);
        for (int i = 0; i < OtpAppConstraints.SECRETSIZE; i++) {
            int index = (int) (finalRandomString.length() * Math.random());
            userSecret.append(finalRandomString.charAt(index));
        }
        return userSecret.toString().toUpperCase();
    }

    public ParamValidatorDao isInvalidParams(OtpBean otpBean) {
        ParamValidatorDao response = new ParamValidatorDao();
        otpBean.setSecret(otpBean.getSecret().trim());
        otpBean.setUserID(otpBean.getUserID().trim());
        if (otpBean.getSecret().isEmpty() || otpBean.getSecret() == null || otpBean.getSecret().equalsIgnoreCase(" ") || otpBean.getSecret().equalsIgnoreCase("null") ||
                otpBean.getUserID().isEmpty() || otpBean.getUserID() == null || otpBean.getUserID().equalsIgnoreCase(" ") || otpBean.getUserID().equalsIgnoreCase("null")) {
            response.setMessage(customMsg.getEmptyParams());
            response.setStatus(OtpAppConstraints.TRUE);
            return response;
        } else if (!isValidSecret(otpBean.getSecret())) {
            response.setMessage(customMsg.getInvalidSecret());
            response.setStatus(OtpAppConstraints.TRUE);
            return response;
        }
        response.setStatus(OtpAppConstraints.FALSE);
        response.setMessage(customMsg.getValidationSuccess());
        return response;

    }

    public ParamValidatorDao isInvalidParam(ValidateOtpDto otpBean) {
        ParamValidatorDao response = new ParamValidatorDao();
        otpBean.setOtp(otpBean.getOtp().trim());
        otpBean.setUserID(otpBean.getUserID().trim());
        if (otpBean.getOtp().isEmpty() || otpBean.getOtp() == null || otpBean.getOtp().equalsIgnoreCase(" ") || otpBean.getOtp().equalsIgnoreCase("null") || otpBean.getUserID().isEmpty() || otpBean.getUserID() == null || otpBean.getUserID().equalsIgnoreCase(" ") || otpBean.getUserID().equalsIgnoreCase("null")) {
            response.setMessage(customMsg.getEmptyParams());
            response.setStatus(OtpAppConstraints.TRUE);
            return response;
        } else if (otpBean.getOtp().length() != 6) {
            response.setMessage(customMsg.otpNaN);
            response.setStatus(OtpAppConstraints.TRUE);
            return response;
        } else {
            try {
                Integer.parseInt(otpBean.getOtp());
                response.setStatus(OtpAppConstraints.FALSE);
                response.setMessage(customMsg.getValidationSuccess());
                return response;
            } catch (NumberFormatException numberFormatException) {
                response.setMessage(customMsg.otpNaN);
                response.setStatus(OtpAppConstraints.TRUE);
                return response;
            }

        }
    }

    public boolean isValidSecret(String secret) {
        final Pattern pattern = Pattern.compile("[A-Za-z0-9+]{16}");
        final Matcher matcher = pattern.matcher(secret);
        return matcher.matches();
    }

    public boolean isAuthenticated(OtpBean otpBean, Optional<RegisteredUser> user) {
        String secretFromDB = decryptSecret(user.get().getSecret(), decoder(user.get().getUserPass()));
        return (secretFromDB.equals(otpBean.getSecret()));
    }

    public OTPResponseDto generateTOTP(OtpBean otpBean, Optional<RegisteredUser> user, long epochTime, String taskName) {
        byte[] sharedSecret;

        sharedSecret = encryptSecret(user.get().getUserPass(), otpBean.getSecret()).getBytes();

        // sharedSecret = encryptSecret(user.get().getUserPass(), user.get().getSecret()).getBytes();

        System.out.println(Arrays.toString(sharedSecret));
        OTPResponseDto responseDto = new OTPResponseDto(OtpAppConstraints.SUCCESS, generateCode(sharedSecret, epochTime), otpBean.getUserID());
        System.out.println("Generate OTP " + generateCode(sharedSecret, epochTime));
        return responseDto;
    }

    private String generateCode(byte[] sharedSecret, long epochTime) {
        long counter = epochTime / 1000 / OtpAppConstraints.OTPVALIDITY;
        byte[] epochBytes = ByteBuffer.allocate(8).putLong(counter).array();
        int code = calculateHmac(sharedSecret, epochBytes);
        String finalOtp = String.format("%06d", code);
        return finalOtp;
    }

    private int calculateHmac(byte[] sharedSecret, byte[] ephochBytes) {
        SecretKeySpec specKey = new SecretKeySpec(sharedSecret, OtpAppConstraints.HMACALGORITHM);
        byte[] encryptedTime;
        int lastByte;
        try {
            Mac mac = Mac.getInstance(OtpAppConstraints.HMACALGORITHM);
            mac.init(specKey);
            encryptedTime = mac.doFinal(ephochBytes);
            lastByte = encryptedTime[encryptedTime.length - 1] & 0xF;
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
        long truncatedHash = 0;
        for (int i = 0; i < 4; i++) {
            truncatedHash <<= 8;
            truncatedHash |= encryptedTime[(lastByte + i)] & 0xFF;
        }

        truncatedHash &= 0x7FFFFFFF;
        truncatedHash %= (int) Math.pow(10, 6);

        return (int) truncatedHash;
    }
}

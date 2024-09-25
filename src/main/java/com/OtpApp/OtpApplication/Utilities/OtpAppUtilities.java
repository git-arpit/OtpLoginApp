package com.OtpApp.OtpApplication.Utilities;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.EncryptUserDto;
import com.OtpApp.OtpApplication.Entities.GenerateOtpBean;
import com.OtpApp.OtpApplication.Entities.OTPResponseDto;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

@Component
public class OtpAppUtilities {

    IvParameterSpec spec = EncryptionUtil.generateIv();

    public String decoder(String encodedString) {

        String decodedPass = new String(Base64.getDecoder().decode(encodedString));

        return decodedPass;
    }

    public String encryptSecret(EncryptUserDto encryptUserDto, String userSecret) {

        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(encryptUserDto.getUserPass(), OtpAppConstraints.SALT);
        return EncryptionUtil.encrypt(OtpAppConstraints.ALGORITHM, userSecret, secretKey, spec);
    }

    public String decryptSecret(String cipher, String decodedPass) {
        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(decodedPass, OtpAppConstraints.SALT);
        try {
            String decrypt = EncryptionUtil.decrypt(OtpAppConstraints.ALGORITHM, cipher, secretKey, spec);
            return decrypt;
        } catch (Exception exception) {
            exception.getMessage();
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


    public boolean isInvalidParams(GenerateOtpBean generateOtpBean) {
        return generateOtpBean.getSecret().isEmpty() || generateOtpBean.getSecret() == null || generateOtpBean.getSecret().equalsIgnoreCase(" ") || generateOtpBean.getSecret().equalsIgnoreCase("null") ||
                generateOtpBean.getUserID().isEmpty() || generateOtpBean.getUserID() == null || generateOtpBean.getUserID().equalsIgnoreCase(" ") || generateOtpBean.getUserID().equalsIgnoreCase("null");

    }


    public boolean isAuthenticated(GenerateOtpBean generateOtpBean, Optional<RegisteredUser> user) {
        String secretFromDB = decryptSecret(user.get().getSecret(), decoder(user.get().getUserPass()));
        return (secretFromDB.equals(generateOtpBean.getSecret()));
    }

    public OTPResponseDto generateTOTP(GenerateOtpBean generateOtpBean, Optional<RegisteredUser> user, long epochTime) {
        byte[] sharedSecret = decryptSecret(user.get().getSecret(), decoder(user.get().getUserPass())).getBytes();
        OTPResponseDto responseDto = new OTPResponseDto(OtpAppConstraints.SUCCESS, generateCode(sharedSecret, epochTime), generateOtpBean.getUserID());
        generateCode(sharedSecret, epochTime);
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

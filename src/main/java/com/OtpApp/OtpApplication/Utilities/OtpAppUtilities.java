package com.OtpApp.OtpApplication.Utilities;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.EncryptUserDto;
import com.OtpApp.OtpApplication.Entities.GenerateOtpBean;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Locale;
import java.util.Optional;

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

    public String decryptSecret(String cipher, String encryptedPass) {
        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(encryptedPass, OtpAppConstraints.SALT);
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

    public void generateTOTP(GenerateOtpBean generateOtpBean) {
        System.out.println(Instant.now().getEpochSecond());
        long epochSecond = Instant.now().getEpochSecond();
        double floor = Math.floor(epochSecond / 60);
        DecimalFormat df = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        String differenceCount = df.format(floor);
        System.out.println(differenceCount);
        String hex = Integer.toHexString(Integer.parseInt(differenceCount));
        System.out.println(hex);
        // append 0 to make hex 16 digit
        // assign to byte array of size 8 - m
        // sign with secret - user shared - k
        //HMAC SHA generated with m and K
        //get last 4 bits of HMAC
        // get its integer(index) value
        //get 4 values post index
        // apply binary operation
        //new binary value
        // convert into integer
        // integer % 10 ki power 6 - 6 digit OTP

    }
}

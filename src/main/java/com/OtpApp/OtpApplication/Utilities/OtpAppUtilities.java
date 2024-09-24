package com.OtpApp.OtpApplication.Utilities;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.EncryptUserDto;
import org.springframework.stereotype.Component;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

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

    public String decryptSecret(String cipher, EncryptUserDto encryptUserDto) throws InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        SecretKey secretKey = EncryptionUtil.getKeyFromPassword(encryptUserDto.getUserPass(), OtpAppConstraints.SALT);

        String decrypt = EncryptionUtil.decrypt(OtpAppConstraints.ALGORITHM, cipher, secretKey, spec);
        System.out.println(decrypt);
        return decrypt;
    }

    public String generateOTPSecret(EncryptUserDto encryptUserDto) {
        String finalRandomString = OtpAppConstraints.RANDOMSTR.concat(encryptUserDto.getUserID()).concat(encryptUserDto.getUserPass());
        StringBuffer userSecret = new StringBuffer(OtpAppConstraints.SECRETSIZE);
        for (int i = 0; i < OtpAppConstraints.SECRETSIZE; i++) {
            int index = (int) (finalRandomString.length() * Math.random());
            userSecret.append(finalRandomString.charAt(index));
        }
        return userSecret.toString().toUpperCase();
    }
}

package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Entities.GenerateOtpBean;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class GenerateOtpService {

    @Autowired
    RegisterRepo registerRepo;

    @Autowired
    OtpAppUtilities otpAppUtilities;

    public void generateOtp(GenerateOtpBean generateOtpBean) {
        if (otpAppUtilities.isInvalidParams(generateOtpBean)) {
            System.out.println("params are invalid");
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(generateOtpBean.getUserID());
            if (user.isPresent()) {
                boolean authenticated = otpAppUtilities.isAuthenticated(generateOtpBean, user);
                if (!authenticated) {
                    System.out.println("Auth Fail");
                } else {
                    otpAppUtilities.generateTOTP(generateOtpBean);
                }
            } else {
                System.out.println("Register kro bhai");
            }

        }
    }
}

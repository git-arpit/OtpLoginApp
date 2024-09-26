package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.OtpBean;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import com.OtpApp.OtpApplication.Entities.ResponseErrorDto;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class GenerateOtpService {

    @Autowired
    private RegisterRepo registerRepo;
    @Autowired
    private OtpAppUtilities otpAppUtilities;


    public Object generateOtpService(OtpBean otpBean) {

        if (otpAppUtilities.isInvalidParams(otpBean)) {
            return new ResponseErrorDto(otpBean.getUserID(), "Invalid parameter passed in request", OtpAppConstraints.FAIL);
        } else {

            Optional<RegisteredUser> user = registerRepo.findById(otpBean.getUserID());
            if (user.isPresent()) {
                {
                    long epochTime = System.currentTimeMillis();
                    return otpAppUtilities.generateTOTP(otpBean, user, epochTime);
                }
            } else {
                return new ResponseErrorDto(otpBean.getUserID(), "OTP Generation failed, please reactivate.", OtpAppConstraints.FAIL);
            }

        }

    }


}




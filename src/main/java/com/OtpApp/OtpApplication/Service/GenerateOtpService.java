package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.GenerateOtpBean;
import com.OtpApp.OtpApplication.Entities.OTPResponseDto;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import com.OtpApp.OtpApplication.Entities.ResponseErrorDto;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GenerateOtpService {

    @Autowired
    RegisterRepo registerRepo;

    @Autowired
    OtpAppUtilities otpAppUtilities;

    public Object generateOtpService(GenerateOtpBean generateOtpBean) {
        if (otpAppUtilities.isInvalidParams(generateOtpBean)) {
            return new ResponseErrorDto(generateOtpBean.getUserID(), "Inavlid parameter passed in request", OtpAppConstraints.FAIL);
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(generateOtpBean.getUserID());
            if (user.isPresent()) {
                boolean authenticated = otpAppUtilities.isAuthenticated(generateOtpBean, user);
                if (!authenticated) {
                    return new ResponseErrorDto(generateOtpBean.getUserID(),"OTP Generation failed, please reactivate.", OtpAppConstraints.FAIL);
                } else {
                    long epochTime = System.currentTimeMillis();
                    return otpAppUtilities.generateTOTP(generateOtpBean, user, epochTime);
                }
            } else {
                return new ResponseErrorDto(generateOtpBean.getUserID(),"OTP Generation failed, please reactivate.", OtpAppConstraints.FAIL);
            }

        }


    }
}


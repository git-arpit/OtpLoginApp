package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.*;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidateOtpService {

    @Autowired
    private RegisterRepo registerRepo;
    @Autowired
    private OtpAppUtilities otpAppUtilities;

    public Object validation(ValidateOtpDto validateOtpDto) {
        if (otpAppUtilities.isInvalidParam(validateOtpDto)) {
            return new ResponseErrorDto(validateOtpDto.getUserID(), "Invalid parameter passed in request", OtpAppConstraints.FAIL);
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(validateOtpDto.getUserID());
            OtpBean otpBean = new OtpBean(user.get().getUserID(), otpAppUtilities.decoder(user.get().getUserPass()));
            if (!user.isPresent()) {
                return new ResponseErrorDto(validateOtpDto.getUserID(), "OTP Generation failed, please activate / reactivate.", OtpAppConstraints.FAIL);
            } else {
                long epochTime = System.currentTimeMillis();
                OTPResponseDto otpResponseDto = otpAppUtilities.generateTOTP(otpBean, user, epochTime);
                if (otpResponseDto.getOtp().equals(validateOtpDto.getOtp())) {
                    return new ValidateResponseDto(otpBean.getUserID(), "Validation Successful", OtpAppConstraints.TRUE);
                } else {
                    return new ValidateResponseDto(otpBean.getUserID(), "Validation Unsuccessful", OtpAppConstraints.FALSE);
                }
            }


        }

    }


}



package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.*;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidateOtpService {

    @Autowired
    private RegisterRepo registerRepo;
    @Autowired
    private OtpAppUtilities otpAppUtilities;
    @Autowired
    private CustomMsg customMsg;

    public Object validation(ValidateOtpDto validateOtpDto, String taskName) {
        ParamValidatorDao invalidParam = otpAppUtilities.isInvalidParam(validateOtpDto);
              if (invalidParam.isStatus()) {
            return new ResponseErrorDto(validateOtpDto.getUserID(),invalidParam.getMessage(), OtpAppConstraints.FAIL);
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(validateOtpDto.getUserID());
            if (!user.isPresent()) {
                return new ResponseErrorDto(validateOtpDto.getUserID(), customMsg.getValidationError(), OtpAppConstraints.FAIL);
            } else {
                OtpBean otpBean = new OtpBean(user.get().getUserID(), otpAppUtilities.decoder(user.get().getUserPass()));
                long epochTime = System.currentTimeMillis();
                OTPResponseDto otpResponseDto = otpAppUtilities.generateTOTP(otpBean, user, epochTime, taskName);
                if (otpResponseDto.getOtp().equals(validateOtpDto.getOtp())) {
                    if(!validateOtpDto.getOtp().equalsIgnoreCase(user.get().getUsedOtp())){
                    user.get().setUsedOtp(otpResponseDto.getOtp());
                    registerRepo.save(user.get());
                    return new ValidateResponseDto(otpBean.getUserID(), customMsg.getValidationSuccess(), OtpAppConstraints.TRUE);}
                    else{
                        return new ValidateResponseDto(otpBean.getUserID(), customMsg.getOtpReused(), OtpAppConstraints.FALSE);                    }
                } else {
                    return new ValidateResponseDto(otpBean.getUserID(), customMsg.getIncorrectOtp(), OtpAppConstraints.FALSE);
                }
            }


        }

    }


}



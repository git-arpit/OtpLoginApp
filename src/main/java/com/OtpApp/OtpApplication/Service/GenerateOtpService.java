package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Bean.OtpBean;
import com.OtpApp.OtpApplication.Bean.ParamValidatorDao;
import com.OtpApp.OtpApplication.Bean.ResponseErrorDto;
import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class GenerateOtpService {

    @Autowired
    private RegisterRepo registerRepo;
    @Autowired
    private OtpAppUtilities otpAppUtilities;
    @Autowired
    private CustomMsg customMsg;


    public Object generateOtpService(OtpBean otpBean, String taskName) {
        ParamValidatorDao invalidParams = otpAppUtilities.isInvalidParams(otpBean);
        if (invalidParams.isStatus()) {
            return new ResponseErrorDto(otpBean.getUserID(), invalidParams.getMessage(), OtpAppConstraints.FAIL);
        } else {

            Optional<RegisteredUser> user = registerRepo.findById(otpBean.getUserID());
            if (user.isPresent() && user.get().getInvalidAttempts() >= OtpAppConstraints.MAX_INVALID_ATTEMPTS)
                {
                    return new ResponseErrorDto(otpBean.getUserID(), "User's Account is Locked", OtpAppConstraints.FAIL);
                }
                if (user.isPresent()) {
                    long epochTime = System.currentTimeMillis();
                    return otpAppUtilities.generateTOTP(otpBean, user, epochTime, taskName);
                }
            return new ResponseErrorDto(otpBean.getUserID(), customMsg.getOtpGenFailure(), OtpAppConstraints.FAIL);
            }


        }


    }




package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Bean.*;
import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.AllUsers;
import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ValidateOtpService {

    @Autowired
    AllUsersRepo allUsersRepo;
    @Autowired
    private RegisterRepo registerRepo;
    @Autowired
    private OtpAppUtilities otpAppUtilities;
    @Autowired
    private CustomMsg customMsg;

    public Object validation(ValidateOtpDto validateOtpDto, String taskName) {
        ParamValidatorDao invalidParam = otpAppUtilities.isInvalidParam(validateOtpDto);
        if (invalidParam.isStatus()) {
            return new ResponseErrorDto(validateOtpDto.getUserID(), invalidParam.getMessage(), OtpAppConstraints.FAIL);
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(validateOtpDto.getUserID());
            if (user.isEmpty()) {
                return new ResponseErrorDto(validateOtpDto.getUserID(), customMsg.getValidationError(), OtpAppConstraints.FAIL);
            } else if (user.get().getInvalidAttempts() >= OtpAppConstraints.MAX_INVALID_ATTEMPTS) {
                return new ResponseErrorDto(validateOtpDto.getUserID(), customMsg.getAccLock(), OtpAppConstraints.FAIL);
            } else {
                OtpBean otpBean = new OtpBean(user.get().getUserID(), user.get().getSecret());
                long epochTime = System.currentTimeMillis();
                OTPResponseDto otpResponseDto = otpAppUtilities.generateTOTP(otpBean, user, epochTime, taskName);
                if (otpResponseDto.getOtp().equals(validateOtpDto.getOtp())) {
                    if (!validateOtpDto.getOtp().equalsIgnoreCase(user.get().getUsedOtp())) {
                        user.get().setUsedOtp(otpResponseDto.getOtp());
                        user.get().setInvalidAttempts(OtpAppConstraints.INIT_COUNTER);
                        registerRepo.save(user.get());
                        return new ValidateResponseDto(otpBean.getUserID(), customMsg.getValidationSuccess(), OtpAppConstraints.TRUE);
                    } else {
                        //
                        if (user.get().getInvalidAttempts() >= OtpAppConstraints.MAX_INVALID_ATTEMPTS || user.get().getInvalidAttempts() == 2) {
                            registerRepo.delete(user.get());
                            allUsersRepo.save(new AllUsers(user.get().getUserID(), OtpAppConstraints.TRUE));
                            return new ValidateResponseDto(otpBean.getUserID(), customMsg.getOtpReused() + " " + customMsg.getAccLock(), OtpAppConstraints.FALSE);
                        }
                        if (user.get().getInvalidAttempts() == 1) {
                            user.get().setInvalidAttempts(user.get().getInvalidAttempts() + 1);
                            registerRepo.save(user.get());
                            return new ValidateResponseDto(otpBean.getUserID(), customMsg.getOtpReused() + " " + customMsg.getAttempt2(), OtpAppConstraints.FALSE);
                        }
                        user.get().setInvalidAttempts(user.get().getInvalidAttempts() + 1);
                        registerRepo.save(user.get());
                        return new ValidateResponseDto(otpBean.getUserID(), customMsg.getOtpReused(), OtpAppConstraints.FALSE);
                        //
                    }
                } else {
                    if (user.get().getInvalidAttempts() >= OtpAppConstraints.MAX_INVALID_ATTEMPTS || user.get().getInvalidAttempts() == 2) {
                        registerRepo.delete(user.get());
                        allUsersRepo.save(new AllUsers(user.get().getUserID(), OtpAppConstraints.TRUE));
                        return new ValidateResponseDto(otpBean.getUserID(), customMsg.getIncorrectOtp() + " " + customMsg.getAccLock(), OtpAppConstraints.FALSE);
                    }
                    if (user.get().getInvalidAttempts() == 1) {
                        user.get().setInvalidAttempts(user.get().getInvalidAttempts() + 1);
                        registerRepo.save(user.get());
                        return new ValidateResponseDto(otpBean.getUserID(), customMsg.getIncorrectOtp() + " " + customMsg.getAttempt2(), OtpAppConstraints.FALSE);
                    }
                    user.get().setInvalidAttempts(user.get().getInvalidAttempts() + 1);
                    registerRepo.save(user.get());
                    return new ValidateResponseDto(otpBean.getUserID(), customMsg.getIncorrectOtp(), OtpAppConstraints.FALSE);
                }
            }


        }

    }


}



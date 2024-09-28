package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Bean.EncryptUserDto;
import com.OtpApp.OtpApplication.Bean.ResponseDto;
import com.OtpApp.OtpApplication.Bean.ResponseErrorDto;
import com.OtpApp.OtpApplication.Bean.UserDto;
import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.*;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import com.OtpApp.OtpApplication.Repository.RegisterRepo;
import com.OtpApp.OtpApplication.Utilities.OtpAppUtilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;

@Service
public class ActivateService {
    @Autowired
    AllUsersRepo allUsersRepo;

    @Autowired
    RegisterRepo registerRepo;

    @Autowired
    OtpAppUtilities otpAppUtilities;

    @Autowired
    CustomMsg customMsg;

    public Object activateUser(UserDto userDto) {

        Optional<AllUsers> userRecord = allUsersRepo.findById(userDto.getUserID());
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserID(userDto.getUserID());
        responseErrorDto.setUserID(userDto.getUserID());
        if (userRecord.isEmpty()) {
            responseErrorDto.setMessage(customMsg.getInvalidUser());
            responseErrorDto.setStatus(OtpAppConstraints.FAIL);
            return responseErrorDto;
        }else if (userRecord.get().isLocked()){
            responseErrorDto.setMessage("User's Account is locked");
            responseErrorDto.setStatus(OtpAppConstraints.FAIL);
            return responseErrorDto;
        }
        if (registerRepo.findById(userDto.getUserID()).isEmpty()) {
            String secret = registerUser(userDto);
            responseDto.setSecret(secret);
            responseDto.setMessage(customMsg.getRegSuccess());
            return responseDto;
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(userDto.getUserID());
            if (otpAppUtilities.decoder(user.get().getUserPass()).equals(userDto.getPassword())) {
                String secret = registerUser(userDto);
                responseDto.setSecret(secret);
                responseDto.setMessage(customMsg.getReactiveSuccess());
                return responseDto;
            }
            return new ResponseErrorDto(userDto.getUserID(), customMsg.getIncorrectOtp(), OtpAppConstraints.FAIL);
        }
    }

    private String registerUser(UserDto userDto) {
        String encodedPass = Base64.getEncoder().encodeToString(userDto.getPassword().getBytes());
        String currentTime = LocalDateTime.now().withNano(0).toString();
        EncryptUserDto encryptUserDto = new EncryptUserDto(userDto.getUserID(), userDto.getPassword(), currentTime);
        String userSecret = otpAppUtilities.generateSecret(encryptUserDto);
        String encodedSecret = otpAppUtilities.encryptSecret(encryptUserDto.getUserPass(), userSecret);
        RegisteredUser registeredUser = new RegisteredUser(userDto.getUserID(), encodedPass, encodedSecret, currentTime,OtpAppConstraints.DEFAULTOTP, OtpAppConstraints.INIT_COUNTER);
        persistUser(registeredUser);
        return userSecret;
    }

    public void persistUser(RegisteredUser registeredUser) {
        registerRepo.save(registeredUser);
    }
}
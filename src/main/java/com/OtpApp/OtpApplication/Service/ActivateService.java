package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.*;
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

    public Object activateUser(UserDto userDto) {

        Optional<AllUsers> userRecord = allUsersRepo.findById(userDto.getUserID());
        ResponseErrorDto responseErrorDto = new ResponseErrorDto();
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserID(userDto.getUserID());
        responseErrorDto.setUserID(userDto.getUserID());
        if (userRecord.isEmpty()) {
            responseErrorDto.setMessage("Failed to generate secret, Invalid user");
            responseErrorDto.setStatus(OtpAppConstraints.FAIL);
            return responseErrorDto;
        }
        if (registerRepo.findById(userDto.getUserID()).isEmpty()) {
            String secret = registerUser(userDto);
            responseDto.setSecret(secret);
            responseDto.setMessage("User Registration Successful");
            return responseDto;
        } else {
            Optional<RegisteredUser> user = registerRepo.findById(userDto.getUserID());
            if (otpAppUtilities.decoder(user.get().getUserPass()).equals(userDto.getPassword())) {
                String secret = registerUser(userDto);
                responseDto.setSecret(secret);
                responseDto.setMessage("Reactivation Successful");
                return responseDto;
            }
            return new ResponseErrorDto(userDto.getUserID(),"Incorrect userID or password.", "Failed");
        }
    }

    private String registerUser(UserDto userDto) {
        String encodedPass = Base64.getEncoder().encodeToString(userDto.getPassword().getBytes());
        String currentTime = LocalDateTime.now().withNano(0).toString();
        EncryptUserDto encryptUserDto = new EncryptUserDto(userDto.getUserID(), userDto.getPassword(), currentTime);
        String userSecret = otpAppUtilities.generateSecret(encryptUserDto);
        String encodedSecret = otpAppUtilities.encryptSecret(encryptUserDto, userSecret);
        RegisteredUser registeredUser = new RegisteredUser(userDto.getUserID(), encodedPass, encodedSecret, currentTime);
        persistUser(registeredUser);
        return userSecret;
    }

    public void persistUser(RegisteredUser registeredUser) {
        registerRepo.save(registeredUser);
    }
}




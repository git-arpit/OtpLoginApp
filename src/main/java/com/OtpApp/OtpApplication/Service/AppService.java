package com.OtpApp.OtpApplication.Service;

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
public class AppService {
    @Autowired
    AllUsersRepo allUsersRepo;

    @Autowired
    RegisterRepo registerRepo;

    @Autowired
    OtpAppUtilities otpAppUtilities;

    public ResponseDto activateUser(UserDto userDto) {
        Optional<AllUsers> userRecord = allUsersRepo.findById(userDto.getUserID());
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserId(userDto.getUserID());
        if (!userRecord.isEmpty()){
        responseDto.setMessage("Failed to generate secret, Invalid user");
        return  responseDto;
        }
        String encodedPass = Base64.getEncoder().encodeToString(userDto.getPassword().getBytes());
        String currentTime = LocalDateTime.now().withNano(0).toString();
        EncryptUserDto encryptUserDto = new EncryptUserDto(userDto.getUserID(), userDto.getPassword(), currentTime);
        String userSecret = otpAppUtilities.generateOTPSecret(encryptUserDto);
        String encodedSecret = otpAppUtilities.encryptSecret(encryptUserDto, userSecret);
        RegisteredUser registeredUser = new RegisteredUser(userDto.getUserID(),encodedPass,encodedSecret, currentTime);
        persistUser(registeredUser);
        responseDto.setSecret(userSecret);
        responseDto.setMessage("User Registration Successful");


        return responseDto;
    }

    public void persistUser(RegisteredUser registeredUser){
        registerRepo.save(registeredUser);
    }
    }



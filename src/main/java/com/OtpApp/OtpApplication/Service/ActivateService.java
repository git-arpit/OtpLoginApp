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
public class ActivateService {
    @Autowired
    AllUsersRepo allUsersRepo;

    @Autowired
    RegisterRepo registerRepo;

    @Autowired
    OtpAppUtilities otpAppUtilities;

    public Object activateUser(UserDto userDto) {

        Optional<AllUsers> userRecord = allUsersRepo.findById(userDto.getUserID());
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserId(userDto.getUserID());
        if (userRecord.isEmpty()) {
            responseDto.setMessage("Failed to generate secret, Invalid user");
            return responseDto;
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
            return new ResponseErrorDto("User Already Registered, Incorrect userID or password.", "Failed");
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

    public void generateOtpService(GenerateOtpBean generateOtpBean) {
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



package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Entities.AllUsers;
import com.OtpApp.OtpApplication.Entities.ResponseDto;
import com.OtpApp.OtpApplication.Entities.UserDto;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class Appservice {
    @Autowired
    AllUsersRepo allUsersRepo;

    public ResponseDto activateUser(UserDto userDto){
        Optional<AllUsers> userRecord = allUsersRepo.findById(userDto.getUserID());
        ResponseDto responseDto = new ResponseDto();
        responseDto.setUserId(userDto.getUserID());
        if (userRecord.isEmpty()){
        responseDto.setMessage("Failed to generate secret, User not found");
        return  responseDto;
        }



        //RegisteredUser registeredUser = new RegisteredUser(userDto.getUserID(),encodedPass,encodedSecret, currentTime);
        responseDto.setMessage("User Registration Successful");

        return responseDto;
    }
}

package com.OtpApp.OtpApplication.Controller;

import com.OtpApp.OtpApplication.Entities.ResponseDto;
import com.OtpApp.OtpApplication.Entities.UserDto;
import com.OtpApp.OtpApplication.Service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpAppController {

    @Autowired
    AppService appservice;

    @PostMapping("/activate")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        ResponseDto responseDto = appservice.activateUser(userDto);

        return new  ResponseEntity<>(responseDto, HttpStatus.OK);
    }

}

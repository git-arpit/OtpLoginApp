package com.OtpApp.OtpApplication.Controller;

import com.OtpApp.OtpApplication.Entities.UserDto;
import com.OtpApp.OtpApplication.Service.Appservice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OtpAppController {

    @Autowired
    Appservice appservice;

    @PostMapping("/activate")
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto){
        appservice.activateUser(userDto);

        return  null;
    }

}

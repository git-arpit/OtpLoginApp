package com.OtpApp.OtpApplication.Controller;

import com.OtpApp.OtpApplication.Bean.OtpBean;
import com.OtpApp.OtpApplication.Bean.UserDto;
import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import com.OtpApp.OtpApplication.Service.ActivateService;
import com.OtpApp.OtpApplication.Service.GenerateOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("app")
public class OtpAppController {

    @Autowired
    AllUsersRepo allUsersRepo;
    @Autowired
    ActivateService appservice;

    @Autowired
    GenerateOtpService generateOtpService;

    @PostMapping({"/activateOtp"})
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        Object responseDto = appservice.activateUser(userDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/generateOtp")
    public ResponseEntity<?> generateOtp(@RequestBody OtpBean otpBean) {

        Object otpResponseDto = generateOtpService.generateOtpService(otpBean, OtpAppConstraints.ACTIVATE);
        return new ResponseEntity<>(otpResponseDto, HttpStatus.OK);
    }

}

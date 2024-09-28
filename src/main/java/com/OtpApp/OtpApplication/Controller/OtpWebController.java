package com.OtpApp.OtpApplication.Controller;

import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Bean.ValidateOtpDto;
import com.OtpApp.OtpApplication.Service.ValidateOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("web")
public class OtpWebController {

    @Autowired
    private ValidateOtpService validateOtpService;

    @PostMapping("validateOtp")
    public ResponseEntity<?> validateOtp(@RequestBody ValidateOtpDto validateOtpDto) {
        Object validation = validateOtpService.validation(validateOtpDto, OtpAppConstraints.VALIDATE);
        return new ResponseEntity<>(validation, HttpStatus.OK);

    }
}

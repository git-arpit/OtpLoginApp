package com.OtpApp.OtpApplication.Controller;

import com.OtpApp.OtpApplication.Entities.AllUsers;
import com.OtpApp.OtpApplication.Entities.OtpBean;
import com.OtpApp.OtpApplication.Entities.UserDto;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import com.OtpApp.OtpApplication.Service.ActivateService;
import com.OtpApp.OtpApplication.Service.GenerateOtpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("app")
public class OtpAppController {

    @Autowired
    AllUsersRepo allUsersRepo;
    @Autowired
    ActivateService appservice;

    @Autowired
    GenerateOtpService generateOtpService;

    @Autowired
    AutoOtpGeneratorService autoOtpGeneratorService;

    @PostMapping({"/activateOtp"})
    public ResponseEntity<?> registerUser(@RequestBody UserDto userDto) {
        Object responseDto = appservice.activateUser(userDto);
        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }

    @PostMapping("/generateOtp")
    public ResponseEntity<?> generateOtp(@RequestBody OtpBean otpBean) {
        Object otpResponseDto = generateOtpService.generateOtpService(otpBean);
        return new ResponseEntity<>(otpResponseDto, HttpStatus.OK);
    }

    @GetMapping("fill")
    public String fillUserData() {
        AllUsers allUsers = new AllUsers("212121");
        AllUsers allUsers1 = new AllUsers("222222");
        AllUsers allUsers2 = new AllUsers("252525");
        AllUsers allUsers3 = new AllUsers("232323");
        AllUsers allUsers4 = new AllUsers("242424");
        AllUsers allUsers5 = new AllUsers("262626");

        allUsersRepo.save(allUsers);
        allUsersRepo.save(allUsers2);
        allUsersRepo.save(allUsers3);
        allUsersRepo.save(allUsers4);
        allUsersRepo.save(allUsers5);

        return "ALl Users table populated";
    }

}

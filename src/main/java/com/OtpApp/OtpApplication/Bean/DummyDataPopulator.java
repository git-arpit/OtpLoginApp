package com.OtpApp.OtpApplication.Bean;

import com.OtpApp.OtpApplication.Entities.AllUsers;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class DummyDataPopulator {
    @Autowired
    AllUsersRepo allUsersRepo;
    @Bean
    public String fillUserData() {
        AllUsers allUsers = new AllUsers("212121", false);
        AllUsers allUsers1 = new AllUsers("222222", false);
        AllUsers allUsers2 = new AllUsers("252525", false);
        AllUsers allUsers3 = new AllUsers("232323", false);
        AllUsers allUsers4 = new AllUsers("242424", true);
        AllUsers allUsers5 = new AllUsers("262626", true);

        allUsersRepo.save(allUsers);
        allUsersRepo.save(allUsers2);
        allUsersRepo.save(allUsers3);
        allUsersRepo.save(allUsers4);
        allUsersRepo.save(allUsers5);

        return "ALl Users table populated";
    }


}

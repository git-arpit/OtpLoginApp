package com.OtpApp.OtpApplication.Repository;

import com.OtpApp.OtpApplication.Entities.RegisteredUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegisterRepo extends JpaRepository<RegisteredUser, String> {

}

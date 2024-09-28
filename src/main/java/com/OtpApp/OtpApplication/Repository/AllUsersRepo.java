package com.OtpApp.OtpApplication.Repository;

import com.OtpApp.OtpApplication.Entities.AllUsers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AllUsersRepo extends JpaRepository<AllUsers, String> {

    public AllUsers findByUserID(String userId);
}

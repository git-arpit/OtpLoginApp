package com.OtpApp.OtpApplication.Service;

import com.OtpApp.OtpApplication.Bean.AccountUnlockBean;
import com.OtpApp.OtpApplication.Bean.ParamValidatorDao;
import com.OtpApp.OtpApplication.Constraints.OtpAppConstraints;
import com.OtpApp.OtpApplication.Entities.AllUsers;
import com.OtpApp.OtpApplication.Properties.CustomMsg;
import com.OtpApp.OtpApplication.Repository.AllUsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountUnlockService {
    @Autowired
    AllUsersRepo allUsersRepo;

    CustomMsg customMsg;

    public ParamValidatorDao unlock(AccountUnlockBean accountUnlockBean) {
        AllUsers userRecord = allUsersRepo.findByUserID(accountUnlockBean.getUserId());
        System.out.println(userRecord);
        System.out.println(userRecord.getUuid().toString());
        if (userRecord.getUuid().toString().equals(accountUnlockBean.getUuid())) {
            userRecord.setLocked(false);
            allUsersRepo.save(userRecord);
            return new ParamValidatorDao(OtpAppConstraints.TRUE, customMsg.getUnlockedSuccess());
        }
        return new ParamValidatorDao(OtpAppConstraints.FALSE, customMsg.getUnlockedFail() );
    }
}

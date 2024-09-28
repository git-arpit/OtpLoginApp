package com.OtpApp.OtpApplication.Bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamValidatorDao {
    private boolean status;
    private String message;
}

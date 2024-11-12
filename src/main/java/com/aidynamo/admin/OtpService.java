package com.aidynamo.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OtpService {


    private final Map<String, String> otpStore = new HashMap<>();
    private final long OTP_EXPIRATION = 5 * 60 * 1000;

    public void saveOtp(String email, String otp) {
        otpStore.put(email, otp);
    }

    public boolean validateOtp(String email, String otp) {
        String storedOtp = otpStore.get(email);
        if (storedOtp != null && storedOtp.equals(otp)) {
            otpStore.remove(email);
            return true;
        }
        return false;
    }
}

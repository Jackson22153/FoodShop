package com.phucx.account.service.phone;

import com.phucx.account.exception.UserNotFoundException;
import com.phucx.account.model.ResponseFormat;

public interface PhoneVerificationService {
    public String generateOTP(String phone);
    public ResponseFormat verifyOTP(String otp, String phone, String userID) throws UserNotFoundException;
}

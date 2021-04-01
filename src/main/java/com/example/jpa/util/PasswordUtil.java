package com.example.jpa.util;

import lombok.experimental.UtilityClass;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@UtilityClass
public class PasswordUtil {

    public String getEncryptPassword(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder ();
        String encryptPassword = bCryptPasswordEncoder.encode (password);

        return encryptPassword;
    }

    public static boolean equalPassword(String password, String encryptedPassword) {
        return BCrypt.checkpw (password, encryptedPassword);
    }
}

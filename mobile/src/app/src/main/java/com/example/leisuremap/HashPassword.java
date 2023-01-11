package com.example.leisuremap;

import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class HashPassword {
    private String password;

    public HashPassword(String password) {
        this.password = password;
    }

    // In the Blowfish cipher the password cannot exceed more 72 bytes in length, 16 byte salt (22 chars) and bcrypt hash (31 chars) encoded with a base64 dialect
    // Will return hashes with version $2a$. By using BCrypt.withDefaults() it will default to version $2a$
    public String hashing() throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {
        String bcryptHashString = BCrypt.withDefaults().hashToString(12, password.toCharArray());
        return bcryptHashString;
    }
}

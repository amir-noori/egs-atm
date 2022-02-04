package com.egs.BankService.util;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class HashUtils {

    private static final Logger log = LogManager.getLogger(HashUtils.class);

    public static String md5(String value) {
        if(StringUtils.isEmpty(value)) {
            return null;
        }
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            byte[] digest = messageDigest.digest(value.getBytes());
            BigInteger no = new BigInteger(1, digest);
            return no.toString(16);
        } catch (NoSuchAlgorithmException e) {
            log.error("cannot encrypt test " + value + " to md5");
            e.printStackTrace();
            return null;
        }
    }

}

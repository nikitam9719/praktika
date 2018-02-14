package com.github.stagirs.common;

import java.security.MessageDigest;

/**
 *
 * @author pc
 */
public class HashUtils {
    public static long hash(String string){
        try {
            return hash(string.getBytes("UTF-8"));
        } catch (Exception e) {
            return 0;
        }
    }
    
    public static long hash(byte[] bytes){
        MessageDigest messageDigest = null;
        byte[] digest;

        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(bytes);
            digest = messageDigest.digest();
        } catch (Exception e) {
            return 0;
        }
        
        long result1 = 0;       
        for(int i = 0; i < 8; i++){
            result1 = (result1 << 8) + digest[i];
        }
        long result2 = 0;       
        for(int i = 8; i < 16; i++){
            result2 = (result2 << 8) + digest[i];
        }
        return result1 ^ result2;
    }
}

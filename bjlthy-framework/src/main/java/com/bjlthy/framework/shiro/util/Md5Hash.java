package com.bjlthy.framework.shiro.util;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.lang.codec.Base64;
import org.apache.shiro.lang.codec.Hex;

public class Md5Hash extends SimpleHash {
    public static final String ALGORITHM_NAME = "MD5";

    public Md5Hash() {
        super("MD5");
    }

    public Md5Hash(Object source) {
        super("MD5", source);
    }

    public Md5Hash(Object source, Object salt) {
        super("MD5", source, salt);
    }

    public Md5Hash(Object source, Object salt, int hashIterations) {
        super("MD5", source, salt, hashIterations);
    }

    public static Md5Hash fromHexString(String hex) {
        Md5Hash hash = new Md5Hash();
        hash.setBytes(Hex.decode(hex));
        return hash;
    }

    public static Md5Hash fromBase64String(String base64) {
        Md5Hash hash = new Md5Hash();
        hash.setBytes(Base64.decode(base64));
        return hash;
    }
}

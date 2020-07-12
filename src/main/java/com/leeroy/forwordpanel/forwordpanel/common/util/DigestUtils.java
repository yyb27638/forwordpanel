package com.leeroy.forwordpanel.forwordpanel.common.util;

/**
 * @Title: DigestUtils
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/9/11 15:09
 */
public class DigestUtils {

    /**
     *
     * 功能描述: MD5加密账号密码
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/9/11 15:11
     */
    public static String Md5(String userName,String password){
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(userName+password);
    }


    public static String Md5(String userName,String password, Long time){
        return org.apache.commons.codec.digest.DigestUtils.md5Hex(userName+password+time);
    }

    public static void main(String[] args) {
        System.out.println(org.apache.commons.codec.digest.DigestUtils.md5Hex("admin"+"123456"));
    }
}

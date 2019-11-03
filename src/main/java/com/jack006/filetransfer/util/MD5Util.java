package com.jack006.filetransfer.util;


import org.apache.commons.codec.digest.DigestUtils;

import java.util.UUID;

/**
 * MD5工具类
 */
public class MD5Util {

    //固定盐
    private static final String salt = "jack006";

    /**
     * 加密方法
     * @param src
     * @return
     */
    public static String md5(String src) {
        return DigestUtils.md5Hex(src);
    }

    /**
     * 将用户输入的明文密码与固定盐进行拼装后再进行MD5加密
     * @param inputPass
     * @return
     */
    public static String inputPassToFormPass(String inputPass) {
        String str = ""+salt.charAt(1)+salt.charAt(3) + inputPass +salt.charAt(2) + salt.charAt(5);
        System.out.println(str);
        return md5(str);
    }

    /**
     * 将form表单中的密码转换成数据库中存储的密码
     * @param formPass
     * @param salt 随机盐
     * @return
     */
    public static String formPassToDBPass(String formPass, String salt) {
        String str = ""+salt.charAt(1)+salt.charAt(3) + formPass +salt.charAt(2) + salt.charAt(5);
        return md5(str);
    }

    public static String inputPassToDbPass(String inputPass, String saltDB) {
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, saltDB);
        return dbPass;
    }

    public static String getSalt(){
        return salt;
    }

}

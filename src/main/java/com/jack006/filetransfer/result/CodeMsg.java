package com.jack006.filetransfer.result;

import lombok.Data;

@Data
public class CodeMsg {

    private int code;
    private String msg;

    //通用的错误码
    public static CodeMsg SUCCESS = new CodeMsg(0, "success");
    public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
    public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
    public static CodeMsg FILE_TYPE_ERROR = new CodeMsg(5001002, "文件格式错误");
    public static CodeMsg FILE_SIZE_ERROR = new CodeMsg(5001003, "文件上传大小错误");
    public static CodeMsg FILE_CREATE_PATH_ERROR = new CodeMsg(5001003, "文件存储路径创建失败");
    public static CodeMsg FILE_CHARACTER_ERROR = new CodeMsg(5001004, "文件包含无效字符");
    public static CodeMsg FILE_SAVE_ERROR = new CodeMsg(5001005, "文件保存错误");
    public static CodeMsg FILE_FIND_ERROR = new CodeMsg(5001006, "文件没有找到");
    public static CodeMsg FILE_DECODE_ERROR = new CodeMsg(5001007, "文件解密失败");
    public static CodeMsg FILE_ENCODE_ERROR = new CodeMsg(5001008, "文件加密失败");
    public static CodeMsg FILE_AES_ERROR = new CodeMsg(5001009, "文件AES失败");
    public static CodeMsg FILE_READ_ERROR = new CodeMsg(5001010, "文件内容读取失败");


    private CodeMsg(){}

    private CodeMsg(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

    public CodeMsg fillArgs(Object...args){
        String message = String.format(this.msg,args);
        return new CodeMsg(this.code,message);
    }

}

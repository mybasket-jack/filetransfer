package com.jack006.filetransfer.result;

import lombok.Data;

/**
 * 对象封装返回
 * @param <T>
 */
@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    private Result(T data){
        this.data = data;
    }

    private Result(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    private Result(CodeMsg codeMsg){
        if (codeMsg != null){
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }

    /**
     * 成功时调用
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Result<T>  success(T data){
        return new Result<T>(data);
    }

    /**
     * 失败的时候调用
     * @param codeMsg
     * @param <T>
     * @return
     */
    public static  <T> Result<T> error(CodeMsg codeMsg){
        return new Result<>(codeMsg);
    }


}

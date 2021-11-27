package com.zyc.zdh.exception;

public class ZdhException extends Exception{

    private int code;

    public ZdhException(String message){
        super(message);
    }

    public ZdhException(int code,String message){
        super(message);
        this.code=code;
    }

    public int getCode(){
        return code;
    }


}

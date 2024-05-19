package com.seu.exception;

public class InvalidInputException extends Exception{
    public InvalidInputException(String msg){
        super("非法输入: " + msg);
    }
}

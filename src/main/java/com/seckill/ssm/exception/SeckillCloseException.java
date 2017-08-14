package com.seckill.ssm.exception;

/**
 * 秒杀关闭异常
 * Created by CycloneBoy on 2017/8/14.
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}

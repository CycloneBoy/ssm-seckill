package com.seckill.ssm.exception;

/**
 * 秒杀相关异常
 * Created by CycloneBoy on 2017/8/14.
 */
public class SeckillException extends RuntimeException{

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

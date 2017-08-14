package com.seckill.ssm.exception;

/**
 * 重复秒杀异常（运行期异常)
 * Created by CycloneBoy on 2017/8/14.
 */
public class RepeatKillException extends  SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.seckill.ssm.service.impl;

import com.seckill.ssm.dao.SeckillDao;
import com.seckill.ssm.dao.SuccessKilledDao;
import com.seckill.ssm.dao.cache.RedisDao;
import com.seckill.ssm.dto.Exposer;
import com.seckill.ssm.dto.SeckillExecution;
import com.seckill.ssm.entity.Seckill;
import com.seckill.ssm.entity.SuccessKilled;
import com.seckill.ssm.enums.SeckillStatEnum;
import com.seckill.ssm.exception.RepeatKillException;
import com.seckill.ssm.exception.SeckillCloseException;
import com.seckill.ssm.exception.SeckillException;
import com.seckill.ssm.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by CycloneBoy on 2017/8/14.
 */
//@Component @Service @Dao @Controller
@Service
public class SeckillServiceImpl implements SeckillService{
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    // 注入service 依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;

    // md5盐值字符串，用于混淆MD5
    private final String slat = "aidjha;afba;fkgab .d;o3up8tuwpiqeofa28380']]^^&^%%%";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,10);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     *
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
       // 优化点:缓存优化 :超时的基础上维护一致性
        // 1. 访问redis
        Seckill seckill  = redisDao.getSeckill(seckillId);
        if(seckill == null){
            // 2. 访问数据库
            seckill =seckillDao.queryById(seckillId);
            if(seckill == null){                                //没有对应的秒杀
                return new Exposer(false,seckillId);
            }else {
                // 3. 放入redis
                redisDao.putSeckill(seckill);
            }
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 系统时间
        Date nowTime = new Date();
        if(nowTime.getTime() < startTime.getTime()          //秒杀还没开始或者秒杀已经结束
                || nowTime.getTime() > endTime.getTime()){
            return  new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),
                    endTime.getTime());
        }
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);  // 秒杀正在进行中，可以进行秒杀操作
    }

    /**
     * 产生混淆后的MD5
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId){
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点：
     * 1. 开发团队达成一致约定，明确标注事务方法的编程风格
     * 2. 保证事务方法的执行时间尽可能短，不要穿插其他的网络操作RPC/HTTP请求或者剥离到事务方法外部
     * 3. 不是所有的方法都需要事务，如只有一条修改操作，只读操作并不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑：减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
            if(updateCount <= 0){
                // 没有更新到记录，秒杀结束
                throw  new SeckillCloseException("seckill is closed");
            }else{
                // 记录秒杀行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
                // 唯一:seckillId,userPhone
                if(insertCount <= 0){
                    // 重复秒杀
                    throw  new RepeatKillException("seckill repeated");
                }else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return  new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }
        }catch (SeckillCloseException e1){
            throw  e1;
        }catch (RepeatKillException e2){
            throw e2;
        } catch (Exception e){
            logger.error(e.getMessage(),e);
            // 所有编译器异常 转化为运行时异常,spring 进行事务回滚
            throw  new SeckillException("seckill inner error : " +e.getMessage());
        }

    }
}

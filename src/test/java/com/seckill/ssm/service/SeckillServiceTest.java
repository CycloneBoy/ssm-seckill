package com.seckill.ssm.service;

import com.seckill.ssm.dto.Exposer;
import com.seckill.ssm.dto.SeckillExecution;
import com.seckill.ssm.entity.Seckill;
import com.seckill.ssm.exception.RepeatKillException;
import com.seckill.ssm.exception.SeckillException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by CycloneBoy on 2017/8/14.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({
        "classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml" })
public class SeckillServiceTest {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long id = 1000L;
        Seckill seckill = seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    @Test
    public void exportSeckillUrl() throws Exception {
        long id = 1001L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        logger.info("exposer={}", exposer);
        /**
         *      秒杀还没开始或者秒杀已经结束：
         * exposer=Exposer{exposed=false,md5='null',seckillId=1000,now=1502702938072,start=1446307200000,
         * end=1446393600000}
         *      秒杀正在进行：
         * exposer=Exposer{exposed=true, md5='15adb5ce81acd54bb1dad83cb162eaea', seckillId=1006, now=0, start=0, end=0}
         *      没有对应的秒杀活动：
         * exposer=Exposer{exposed=false, md5='null', seckillId=1016, now=0, start=0, end=0}
         */
    }

    @Test
    public void executeSeckill() throws Exception {
        long id = 1006L;
        long phone=13018016263L;
        String md5 = "15adb5ce81acd54bb1dad83cb162eaea";
        try {
            SeckillExecution execution  = seckillService.executeSeckill(id,phone,md5);
            logger.info("result={}",execution);
        } catch (RepeatKillException e) {
            logger.error(e.getMessage());
        }catch (SeckillException e) {
            logger.error(e.getMessage());
        }

        /**
         * result=SeckillExecution{seckillId=1006, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1006, userPhone=13018016263, state=0, createTime=Mon Aug 14 17:50:04 CST 2017}}
         */
    }

    // 集成测试代码完整逻辑，注意可以重复执行。
    @Test
    public void testSeckillLogic() throws  Exception{
        long id = 1019L;
        Exposer exposer = seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("exposer={}", exposer);
            long phone=13018016263L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution  = seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",execution);
                /**
                 * 秒杀成功
                 * result=SeckillExecution{seckillId=1009, state=1, stateInfo='秒杀成功', successKilled=SuccessKilled{seckillId=1009, userPhone=13018016263, state=0, createTime=Mon Aug 14 18:03:15 CST 2017}}
                 */
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
                /**
                 * 重复秒杀
                 * seckill repeated
                 */
            }catch (SeckillException e) {
                logger.error(e.getMessage());
                /**
                 * 其他错误
                 * exposer=Exposer{exposed=false, md5='null', seckillId=1019, now=0, start=0, end=0}
                 */
            }
        }else {
            //秒杀未开始
            /**
             *exposer=Exposer{exposed=false, md5='null', seckillId=1008, now=1502704900931, start=1509465600000,
             * end=1509552000000}
             */
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void executeSeckillProcedure(){
        long seckillId = 1007L;
        long phone = 13018016265L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }
    }

}
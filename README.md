# ssm-seckill
商品秒杀小项目学习:SSM

课程须知
《Java高并发秒杀API》是系列课程，共四门课，分别为业务分析和DAO层，Service层，Web层和高并发优化。本门课程是第一门课程，学习前需要了解如下知识：
1、精通JavaWeb基础
2、熟悉SpringMVC、Spring和MyBatis框架
老师告诉你能学到什么？
1、掌握秒杀业务
2、能够进行SpringMVC+Spring+MyBatis的整合开发
3、能够进行秒杀业务DAO层的设计与实现


# Dao 层开发
简介：高并发和秒杀都是当今的热门词汇，如何使用Java框架实现高并发秒杀API是该系列课程要研究的内容。秒杀系列课程分为四门，本门课程是第一门，主要对秒杀业务进行分析设计，
以及DAO层的实现。课程中使用了流行的框架组合SpringMVC+Spring+MyBatis，还等什么，赶快来加入吧！

# Service 层开发
简介：本门课程是《Java实现高并发秒杀API》系列课程的第二门课，主要介绍秒杀业务Service层的设计和实现，基于Spring托管Service实现类，
并使用了Spring声明式事务。秒杀项目使用流行的SpringMVC+Spring+MyBatis整合框架进行开发。非常值得学习呦！

# Web层开发
简介：本门课程是《Java实现高并发秒杀API》系列课程的第三门课，主要介绍秒杀业务Web层的设计和实现，使⽤用SpringMVC整合spring,实现秒杀restful接⼝。
秒杀项目使用流行的SpringMVC+Spring+MyBatis整合框架进行开发。非常值得学习呦！

# 高并发优化
好久没看，在这里说两句：

1.redis事务与RDBMS事务有本质区别，详情见:http://redis.io/topics/transactions

2:关于spring整合redis。原生Jedis API已经足够清晰。笔者所在的团队不使用任何spring-data整合API，而是直接对接原生Client并做二次开发调优，如Jedis,Hbase等.

3:这里使用redis缓存方法用于暴露秒杀地址场景，该方法存在瞬时压力，为了降低DB的primary key QPS，且没有使用库存字段所以不做一致性维护。这里补充一下。

4:跨数据源的严格一致性需要2PC支持，性能不尽如人意。线上产品一般使用最终一致性去解决，这块相关知识较多，所以没有讲。

5:本课程的重点其实不是SSM，只是一个快速开发的方式。重点根据业务场景分析通信成本，瓶颈点的过程和优化思路。

6:初学者不要纠结于事务。事务可以降低一致性维护难度，但扩展性灵活性存在不足。技术是死的，人是活的。比如京东抢购使用Redis+LUA+MQ方案，就是一种技术反思。
遇到的问题
---
项目中遇到的问题总结如下：

1、mvn -archetype:create 构建项目失败，改为mvn -archetype：generate问题解决。

2、MySQL创建表报错：timestamp类型的数据只能有一个default或者update列，把有default值的create_time字段放在前面问题解决。

3、配置数据库连接池报错，jdbc.properties里面的username改为jdbc.username问题解决。(这里我遇到过此问题)
    
4、项目jdk1.6报错，换成jdk1.7问题解决。

虽然都查到解决方法了，但是具体原因还是没有搞清楚，所以先记录一下。


我按照老师的爆了一个连接数据库错误，最好在jdbc.properties中需要修改前面加个前缀jdbc。防止与全局变量冲突，

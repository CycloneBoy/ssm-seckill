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

遇到的问题
---
项目中遇到的问题总结如下：

1、mvn -archetype:create 构建项目失败，改为mvn -archetype：generate问题解决。

2、MySQL创建表报错：timestamp类型的数据只能有一个default或者update列，把有default值的create_time字段放在前面问题解决。

3、配置数据库连接池报错，jdbc.properties里面的username改为jdbc.username问题解决。(这里我遇到过此问题)
    
4、项目jdk1.6报错，换成jdk1.7问题解决。

虽然都查到解决方法了，但是具体原因还是没有搞清楚，所以先记录一下。


我按照老师的爆了一个连接数据库错误，最好在jdbc.properties中需要修改前面加个前缀jdbc。防止与全局变量冲突，

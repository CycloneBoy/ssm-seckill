-- 使用数据库
use seckill;
-- 创建秒杀库存表
CREATE  TABLE seckill(
  `seckill_id` BIGINT not null auto_increment comment '商品库存id',
  `name` varchar(120) not null COMMENT '商品名称',
  `number` int not null COMMENT '商品数量',
  `start_time` timestamp not null COMMENT '秒杀开始时间',
  `end_time` TIMESTAMP not null comment '秒杀结束时间',
  `create_time` TIMESTAMP not null default current_timestamp COMMENT '创建时间',
  primary key (seckill_id),
  key idx_start_time (start_time),
  key idx_end_time(end_time),
  key idx_create_time(create_time)
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀库存表';

-- 初始化数据
insert into
  seckill(name,number,start_time,end_time)
VALUES
  ('100元秒杀iphone6',100,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('500元秒杀ipad2',200,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('300元秒杀小米4',300,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('200元秒杀红米Note',500,'2015-11-01 00:00:00','2015-11-02 00:00:00'),
  ('1元秒杀小米Max',100,'2015-11-01 00:00:00','2015-11-02 00:00:00');

-- 秒杀成功明细表
-- 用户登录认证相关的信息
CREATE  TABLE  success_killed(
  `seckill_id` BIGINT not null COMMENT '秒杀商品id',
  `user_phone` bigint not null COMMENT  '用户手机号',
  `state` tinyint not null DEFAULT  -1 comment '状态表示：-1：无效 0：成功 1:已付款 2：已发货',
  `create_time` TIMESTAMP not null COMMENT  '创建时间',
  primary key (seckill_id,user_phone), /*联合主键*/
  key idx_create_time (create_time)
)ENGINE = InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT '秒杀成功明细表';

-- 连接数据库控制台
mysql -uroot -p123456

create table if not exists usercenter.user
(
    username     varchar(256)                       null comment '用户名',
    id           bigint auto_increment comment '用户id'
    primary key,
    userAccount  varchar(256)                       not null comment '用户账号',
    avatarUrl    varchar(1024)                      null comment '用户头像',
    gender       tinyint                            null comment '性别',
    userPassword varchar(512)                       null comment '密码',
    phone        varchar(128)                       null comment '电话',
    email        varchar(512)                       null comment '邮箱',
    userStatus   int      default 0                 null comment '状态',
    createTime   datetime default CURRENT_TIMESTAMP null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP null comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除',
    userRole     int      default 0                 not null comment '普通用户-0 管理员-1'
    )
    collate = utf8mb4_bin;


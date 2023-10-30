create table order_department
(
    id          int auto_increment
        primary key,
    dept_id     varchar(30)       null comment '部门编号',
    dept_name   varchar(30)       null comment '部门名称',
    entity_id   bigint            null comment '关联主体',
    remark      bigint            null comment '备注',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户--角色关联表' collate = utf8mb4_unicode_ci;

create table order_logs
(
    id        int auto_increment
        primary key,
    operation varchar(255) null comment '操作名称',
    type      varchar(30)  null comment '操作类型',
    ip        varchar(30)  null comment 'ip地址',
    user      varchar(30)  null comment '操作人',
    time      varchar(30)  null comment '操作时间'
)
    comment '系统日志' collate = utf8mb4_unicode_ci;

create table order_menu
(
    id          int auto_increment
        primary key,
    menu_name   varchar(255)      null comment '菜单名称',
    menu_path   varchar(30)       null comment '访问路径',
    menu_type   varchar(30)       null comment '菜单类型 (M: 目录 C:子菜单 B:按钮)',
    menu_pid    int               null comment '菜单父类型',
    icon        varchar(30)       null,
    sort_no     int               null comment '序号（排序）',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '菜单信息表' collate = utf8mb4_unicode_ci;

create table order_role
(
    id          int auto_increment
        primary key,
    role_name   varchar(255)      null comment '角色名称',
    role_type   varchar(30)       null comment '菜单类型',
    role_desc   varchar(30)       null comment '角色描述',
    status      tinyint default 0 null comment '角色状态 (0 停用 1 启用)',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '角色信息表' collate = utf8mb4_unicode_ci;

create table order_role_menu
(
    id          int auto_increment
        primary key,
    role_id     varchar(30)       null comment '角色ID',
    menu_id     varchar(30)       null comment '菜单ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '角色--菜单关联表' collate = utf8mb4_unicode_ci;

create table order_user
(
    id          bigint auto_increment comment '主键'
        primary key,
    openid      varchar(45)       null comment '微信用户唯一标识',
    username    varchar(32)       null comment '账号',
    password    varchar(255)      not null comment '密码',
    realname    varchar(30)       null comment '昵称（姓名）',
    sex         varchar(2)        null comment '性别',
    phone       varchar(11)       null comment '手机号',
    email       varchar(30)       null comment '邮箱号',
    id_number   varchar(18)       null comment '身份证号',
    status      tinyint default 0 null comment '状态 （0 启用 1 停用）',
    avatar      varchar(500)      null comment '头像',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '更新时间',
    create_user varchar(30)       null comment '创建人',
    update_user varchar(30)       null comment '修改人',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户表' collate = utf8mb4_unicode_ci;

create table order_user_role
(
    id          int auto_increment
        primary key,
    user_id     varchar(30)       null comment '用户ID',
    role_id     varchar(30)       null comment '角色ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户--角色关联表' collate = utf8mb4_unicode_ci;


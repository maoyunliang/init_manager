create table department_1
(
    id              int auto_increment
        primary key,
    department_name varchar(255)      null comment '部门名称',
    depart_no       varchar(255)      null comment '部门编号',
    remark          varchar(256)      null comment '备注',
    sort_no         bigint            null comment '排序',
    status          tinyint default 1 not null comment '1正常 -1停用',
    pid             bigint            null comment '父id',
    leader          bigint            null comment '部门负责人',
    username        varchar(30)       null comment '部门负责人',
    create_user     varchar(30)       null comment '操作人',
    update_user     varchar(30)       null comment '更新人',
    create_time     datetime          null comment '创建时间',
    update_time     datetime          null comment '创建时间',
    is_del          tinyint default 0 null comment '是否删除'
)
    comment '部门信息表' collate = utf8mb4_unicode_ci;

create table public_login_logs
(
    id        int auto_increment
        primary key,
    operation varchar(255) null comment '操作名称',
    type      varchar(30)  null comment '操作类型',
    ip        varchar(30)  null comment 'ip地址',
    user      varchar(30)  null comment '操作人',
    duration  double       null comment '操作耗时',
    time      varchar(30)  null comment '操作时间'
)
    comment '系统登录日志' collate = utf8mb4_unicode_ci;

create table public_logs
(
    id        int auto_increment
        primary key,
    operation varchar(255) null comment '操作名称',
    type      varchar(30)  null comment '操作类型',
    ip        varchar(30)  null comment 'ip地址',
    user      varchar(30)  null comment '操作人',
    duration  double       null comment '操作耗时',
    time      varchar(30)  null comment '操作时间',
    tenant_id int          null comment '租户id'
)
    comment '系统日志' collate = utf8mb4_unicode_ci;

create table public_menu
(
    id          int auto_increment
        primary key,
    menu_name   varchar(255)      null comment '菜单名称',
    menu_path   varchar(30)       null comment '组件路径',
    menu_router varchar(30)       null comment '菜单路由',
    identify    varchar(30)       null comment '权限标识',
    menu_type   varchar(30)       null comment '菜单类型 (M: 目录 C:子菜单 B:按钮)',
    menu_pid    int               null comment '菜单父类型',
    icon        varchar(30)       null,
    sort_no     int               null comment '序号（排序）',
    status      tinyint           null comment '(1、启用，-1、停用)',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '菜单信息表' collate = utf8mb4_unicode_ci;

create table public_tenant
(
    id            int auto_increment
        primary key,
    tenant_id     varchar(30)       null comment '租户编号',
    tenant_name   varchar(30)       null comment '租户名称',
    tenant_logo   varchar(255)      null comment '主体logo',
    location      varchar(30)       null comment '所在地区',
    contact       varchar(30)       null comment '联系方式',
    address       varchar(30)       null comment '详细地址',
    qualification varchar(255)      null comment '相关资质',
    remark        varchar(255)      null comment '备注',
    status        bigint  default 1 null,
    create_user   varchar(30)       null comment '操作人',
    update_user   varchar(30)       null comment '更新人',
    create_time   datetime          null comment '创建时间',
    update_time   datetime          null comment '创建时间',
    is_del        tinyint default 0 null comment '是否删除'
)
    comment '租户表' collate = utf8mb4_unicode_ci;

create table public_user
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

create table public_user_tenant
(
    id          int auto_increment
        primary key,
    user_id     varchar(30)       null comment '用户ID',
    tenant_id   varchar(30)       null comment '租户ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户--租户关联表' collate = utf8mb4_unicode_ci;

create table role_1
(
    id          int auto_increment
        primary key,
    role_name   varchar(255)      null comment '角色名称',
    identity    varchar(30)       null comment '权限标识',
    role_type   varchar(30)       null comment '菜单类型',
    role_desc   varchar(30)       null comment '角色描述',
    status      tinyint default 1 null comment '角色状态 (0 停用 1 启用)',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '角色信息表' collate = utf8mb4_unicode_ci;

create table role_menu_1
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

create table user_department_1
(
    id          int auto_increment
        primary key,
    user_id     varchar(30)       null comment '用户ID',
    dept_id     varchar(30)       null comment '部门ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户--部门关联表' collate = utf8mb4_unicode_ci;


create table user_role_1
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



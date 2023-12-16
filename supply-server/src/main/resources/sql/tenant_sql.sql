create table category_2
(
    id            int auto_increment
        primary key,
    category_name varchar(255)      null comment '类目名称',
    pid           bigint            null comment '父id',
    sort_no       varchar(255)      null comment '编号',
    remark        varchar(500)      null comment '备注',
    status        tinyint default 1 not null comment '状态 1正常 0停用',
    create_user   varchar(30)       null comment '操作人',
    update_user   varchar(30)       null comment '更新人',
    create_time   datetime          null comment '创建时间',
    update_time   datetime          null comment '创建时间',
    is_del        tinyint default 0 null comment '是否删除'
)
    comment '类目信息表' collate = utf8mb4_unicode_ci;

create table commodity_2
(
    id             int auto_increment
        primary key,
    commodity_name varchar(255)      null comment '商品名称',
    commodity_no   varchar(255)      null comment '商品编号',
    category_id    bigint            null comment '类目id',
    unit           varchar(30)       null comment '单位',
    remark         varchar(500)      null comment '备注',
    status         tinyint default 1 not null comment '状态 1正常 0停用',
    create_user    varchar(30)       null comment '操作人',
    update_user    varchar(30)       null comment '更新人',
    create_time    datetime          null comment '创建时间',
    update_time    datetime          null comment '创建时间',
    is_del         tinyint default 0 null comment '是否删除'
)
    comment '商品信息表' collate = utf8mb4_unicode_ci;

create table department_2
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

create table role_2
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

create table role_department_2
(
    id          int auto_increment
        primary key,
    role_id     varchar(30)       null comment '角色ID',
    dept_id     varchar(30)       null comment '部门ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '角色--部门关联表' collate = utf8mb4_unicode_ci;

create table role_menu_2
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

create table tender_2
(
    id          int auto_increment
        primary key,
    tender_no   varchar(255)      null comment '招标单号',
    tender_name varchar(30)       null comment '招标名称',
    begin_time  datetime          null comment '招标开始时间',
    end_time    datetime          null comment '招标结束时间',
    remark      varchar(255)      null comment '备注',
    tender_list varchar(255)      null comment '招标清单',
    status      tinyint           null comment '招标状态(1草稿, 2进行中,3已结束)',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '更新时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '用户招标表' collate = utf8mb4_unicode_ci;

create table tender_department_2
(
    id          int auto_increment
        primary key,
    tender_id   varchar(30)       null comment '招标ID',
    dept_id     varchar(30)       null comment '部门ID',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '招标--部门关联表' collate = utf8mb4_unicode_ci;

create table tender_supplier_2
(
    id          int auto_increment
        primary key,
    tender_id   varchar(30)       null comment '招标ID',
    supplier_id varchar(30)       null comment '供应商ID',
    quoted      bigint            null comment '是否报价 -1, 1',
    create_user varchar(30)       null comment '操作人',
    update_user varchar(30)       null comment '更新人',
    create_time datetime          null comment '创建时间',
    update_time datetime          null comment '创建时间',
    is_del      tinyint default 0 null comment '是否删除'
)
    comment '招标--供应商关联表' collate = utf8mb4_unicode_ci;

create table user_department_2
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

create table user_role_2
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


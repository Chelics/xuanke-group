create table checking
(
    id             int auto_increment comment '主键'
        primary key,
    course_id      int                          null comment '关联课程主键',
    course_num     char(8)                      null comment '课程编号',
    course_name    varchar(30)                  null comment '课程名',
    type           tinyint unsigned             null comment '课程类别',
    course_hour    tinyint unsigned             null comment '课时',
    course_storage tinyint unsigned             null comment '课程容量',
    class_ids      varchar(300)                 null comment '班级',
    teacher_ids    varchar(30)                  null comment '教师',
    faculty        varchar(30)                  null comment '学院',
    credit         tinyint unsigned             null comment '学分',
    style          tinyint unsigned             not null comment '操作类型',
    course_status  tinyint unsigned default '1' not null comment '审核状态',
    commit_time    datetime                     not null comment '提交时间'
)
    comment '待审核课程表';

create table class
(
    id         int auto_increment comment '主键'
        primary key,
    class_name char(6) null comment '班级名称'
)
    comment '班级表';

create table course
(
    id             int auto_increment comment '主键'
        primary key,
    course_name    varchar(30)      not null comment '课程名',
    type           tinyint unsigned null comment '类别',
    course_number  char(8)          not null comment '课程编号',
    room_id        int              not null comment '教室主键',
    course_hour    tinyint unsigned null comment '学时',
    course_storage tinyint unsigned null comment '课程容量',
    start_week     tinyint unsigned null comment '开始周',
    end_week       tinyint unsigned null comment '结束周',
    time_1         tinyint unsigned null comment '周内时间1',
    time_2         tinyint unsigned null comment '周内时间2',
    time_3         tinyint unsigned null comment '周内时间3',
    faculty        varchar(30)      null comment '学院',
    credit         tinyint unsigned null comment '学分',
    constraint course_cour_num_uindex
        unique (course_number)
)
    comment '课程表';

create table course_class
(
    id        int auto_increment comment '主键'
        primary key,
    course_id int not null comment '课程主键',
    class_id  int not null comment '课程主键'
)
    comment '班级课表';

create table course_student
(
    id         int auto_increment comment '主键'
        primary key,
    course_id  int not null comment '课程主键',
    student_id int not null comment '学生主键'
)
    comment '学生课表';

create table course_teacher
(
    id         int auto_increment comment '主键'
        primary key,
    course_id  int not null comment '课程主键',
    teacher_id int not null comment '教师主键'
)
    comment '教师课表';

create index course_teacher_course_id_index
    on course_teacher (course_id);

create index course_teacher_teacher_id_index
    on course_teacher (teacher_id);

create table room
(
    id           int auto_increment comment '主键'
        primary key,
    building     varchar(30)      null comment '教学楼',
    room_name    varchar(30)      null comment '教室名',
    room_storage tinyint unsigned null comment '教室容量'
)
    comment '教室表';

create table staff
(
    id             int auto_increment comment '主键'
        primary key,
    username       varchar(30) not null comment '用户名',
    staff_name     varchar(30) not null comment '教务名',
    staff_password varchar(30) not null comment '密码',
    constraint staff_username_uindex
        unique (username)
)
    comment '教务表';

create table stage
(
    id         int auto_increment
        primary key,
    stage_name varchar(30) not null,
    start_time datetime    not null,
    end_time   datetime    not null
);

create table student
(
    id               int auto_increment comment '主键'
        primary key,
    username         varchar(30) not null comment '账户名',
    student_name     varchar(30) not null comment '学生名',
    student_password varchar(30) not null comment '密码',
    class_id         int         not null comment '课程主键',
    constraint student_username_uindex
        unique (username)
)
    comment '学生表';

create table teacher
(
    id               int auto_increment comment '主键'
        primary key,
    username         varchar(30) not null comment '账户名',
    teacher_name     varchar(30) not null comment '教师名',
    teacher_password varchar(30) not null comment '密码',
    constraint teacher_username_uindex
        unique (username)
)
    comment '教师表';



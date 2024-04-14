create database if not exists demo
default character set utf8;
use demo;

create table students(
    id int not null primary key auto_increment,
    student_name varchar(20) not null,
    student_num int not null unique,
    gender tinyint not null,
    parent_name varchar(20),
    parent_tel varchar(11),
    class_id int
) engine=InnoDB;

create table classes(
    id int not null primary key,
    class_name varchar(20) not null,
    chinese_teacher varchar(20),
    math_teacher varchar(20),
    english_teacher varchar(20)
) engine=InnoDB;

create table score(
    id int not null primary key auto_increment,
    student_num int not null,
    chinese_score tinyint default 0,
    math_score tinyint default 0,
    english_score tinyint default 0,
    total_score smallint default 0,
    entry_event datetime DEFAULT CURRENT_TIMESTAMP,
    academic_year smallint,
    semester tinyint,
    class_id int
) engine=InnoDB;

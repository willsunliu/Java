create database if NOT exists sh_data;
use sh_data;

create table if NOT exists good_table
(
	good_name varchar(255),
	good_unit_price float
);
insert into good_table
values
("红色", 35);
insert into good_table
values
("白色", 30);
insert into good_table
values
("蓝色", 40);
create table if NOT exists client_table
(
	client_name varchar(255),
	client_addr varchar(255),
	client_phone int
);

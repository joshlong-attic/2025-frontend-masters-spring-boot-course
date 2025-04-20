insert into users( username, password, enabled) values ('josh', '843b103f6c09de407d366f8ff6553691767a35dbaf870cad47e86945c4103be85ee8ea49509b9502',true);
insert into users( username, password, enabled) values ('rob', 'a94f80ed1a6677dedb489a6912e6c83a7c2a7ada2c4d739dd4a4ab9e454d3d875134fa4480b19c55',true);
insert into users( username, password, enabled) values ('accountant', 'a94f80ed1a6677dedb489a6912e6c83a7c2a7ada2c4d739dd4a4ab9e454d3d875134fa4480b19c55',true);

insert into authorities (username, authority) values ('josh', 'ROLE_USER');
insert into authorities (username, authority) values ('rob', 'ROLE_USER');
insert into authorities (username, authority) values ('rob', 'ROLE_ADMIN');
insert into authorities (username, authority) values ('accountant', 'ROLE_ACCOUNTANT');

update users set password = '{sha256}'|| password;
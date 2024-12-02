
create table if not exists todo (
                                       id int auto_increment primary key ,
                                       text varchar(255) null,
                                       is_active  boolean                     null
);


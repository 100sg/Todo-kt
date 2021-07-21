create table if not exists todos (
id BIGINT AUTO_INCREMENT,
content varchar(2000),
created_at timestamp,
done boolean,
modified_at timestamp,
primary key (id) );

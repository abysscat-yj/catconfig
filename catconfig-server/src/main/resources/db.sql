
create table if not exists `configs` (
    `app` varchar(64) not null,
    `env` varchar(64) not null,
    `ns` varchar(64) not null,
    `pkey` varchar(64) not null,
    `pval` varchar(128) null
);

insert into configs (app, env, ns, pkey, pval) values ('app1', 'dev', 'public', 'cat.a', 'dev100');
insert into configs (app, env, ns, pkey, pval) values ('app1', 'dev', 'public', 'cat.b', 'http://localhost:9129');
insert into configs (app, env, ns, pkey, pval) values ('app1', 'dev', 'public', 'cat.c', 'cc100');


CREATE TABLE IF NOT EXISTS `locks`(
                                      `id` INT PRIMARY KEY NOT NULL,
                                      `app` VARCHAR(64) NOT NULL
);
INSERT INTO LOCKS (id, app) VALUES (1, 'catconfig-server');

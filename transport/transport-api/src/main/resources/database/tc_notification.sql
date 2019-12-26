-- auto-generated definition
create table tc_notification
(
    notification_id   int auto_increment
        primary key,
    status_read       tinyint(1) default 0 not null,
    content           varchar(500)         not null,
    created_date      timestamp            null,
    last_updated_date timestamp            null,
    type              int                  null,
    created_by        int                  null,
    last_updated_by   int                  null,
    main_url          varchar(255)         null
);


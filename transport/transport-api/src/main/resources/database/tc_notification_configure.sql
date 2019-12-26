create table tc_notification_configure
(
    notification_configure_id int auto_increment,
    tc_bvv_id                 int                   not null,
    notification_id           int                   not null,
    constraint tc_notification_configure_pk
        primary key (notification_configure_id)
);

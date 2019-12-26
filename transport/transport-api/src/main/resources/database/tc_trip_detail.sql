create table tc_trip_detail
(
    tc_trip_id        int auto_increment,
    tc_vtt_ctv_id     int       not null comment 'FK: Vtc Ctv',
    lenh_id           int       not null comment 'FK: Tc Lenh',
    distance          decimal   null,
    discount_range    decimal   null,
    status            int       null,
    amount            decimal   null,
    created_date      timestamp null,
    last_updated_date timestamp null,
    created_by        int       null,
    last_updated_by   int       null,
    constraint tc_trip_detail_pk
        primary key (tc_trip_id)
);


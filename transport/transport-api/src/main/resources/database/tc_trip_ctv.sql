create table tc_trip_ctv
(
    trip_ctv_id    int auto_increment,
    tc_vtt_ctv_id  int not null,
    price_group_id int not null,
    constraint tc_trip_ctv_pk
        primary key (trip_ctv_id)
);


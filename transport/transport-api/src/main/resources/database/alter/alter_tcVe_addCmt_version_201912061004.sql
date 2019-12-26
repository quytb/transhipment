alter table tc_ve
    modify COLUMN tc_trang_thai_don integer(1) 
        COMMENT '0: chưa điều, 1: đã điều, 2: đang đi đón, 3: đã đón, 4: đã hủy, 5: Lên Xe'

alter table tc_ve
	add version int default 0 not null;
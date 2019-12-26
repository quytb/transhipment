-- Create table: tc_lenh
CREATE TABLE IF NOT EXISTS tc_lenh (
    tc_lenh_id INT AUTO_INCREMENT PRIMARY KEY,
    lai_xe_id INT(11) NOT NULL COMMENT "FK: admin_lv2_user.adm_id",
		trang_thai INT(11) NOT NULL COMMENT "1: lệnh đã điều, 2: lệnh đang chạy, 3: Lệnh đã hủy, 4: Lệnh đã hoàn tất, 5: lệnh đã tạo",
		chi_tiet_lenh text COMMENT "Json: danh sách vé của lệnh",
		ghi_chu VARCHAR(255)
);

-- Create table: tc_ve
CREATE TABLE IF NOT EXISTS tc_ve (
    tc_ve_id INT AUTO_INCREMENT PRIMARY KEY,
    bvv_id INT(11) NOT NULL COMMENT "FK: ban_ve_ve.bvv_id",
		did_id INT(11) NOT NULL COMMENT "FK: dieu_do_temp.did_id tương đương: ban_ve_ve.bvv_bvn_id",
		is_tc_don INT(1) COMMENT "Default: 0, 1: Có trung chuyển đón",
		is_tc_tra INT(1) COMMENT "Default: 0, 1: Có trung chuyển trả",
		bvv_ten_khach_hang_di VARCHAR(255),
		bvv_phone_di VARCHAR(255),
		bvv_diem_don_khach VARCHAR(255),
		bvv_diem_tra_khach VARCHAR(255),
		bvv_ma_ghe VARCHAR(255),
		bvv_ghi_chu text,
		ve_action INT(1) COMMENT "1: vé tạo mới, 2: vé update, 3: vé xuống xe, 5: Hủy vé",
		tc_lenh_id INT(11) COMMENT "FK: tc_lenh.tc_lenh_id",
		tc_trang_thai_don INT(1) COMMENT "0: chưa điều, 1: đã điều, 2: đang đi đón, 3: đã đón, 4: đã hủy",
		ly_do_tu_choi_don VARCHAR(255),
		thu_tu_don INT(11),
		thoi_gian_don INT(11),
		lai_xe_id_don INT(11) COMMENT "FK: admin_lv2_user.adm_id",
		ghi_chu_don text,
		tc_trang_thai_tra INT(1) COMMENT "0: chưa điều, 1: đã điều, 2: đang đi trả, 3: đã trả, 4: đã hủy",
		ly_do_tu_choi_tra VARCHAR(255),
		thu_tu_tra INT(11),
		thoi_gian_tra INT(11),
		lai_xe_id_tra INT(11) COMMENT "FK: admin_lv2_user.adm_id",
		ghi_chu_tra text,
		tc_khach_hang_moi INT(1) COMMENT "0: Cũ, 1: Mới"
);

-- Create table: tc_configuration
CREATE TABLE IF NOT EXISTS tc_configuration (
    config_key VARCHAR(255) PRIMARY KEY,
    config_value text,
		description VARCHAR(255)
);
INSERT INTO tc_configuration (config_key,config_value,description) VALUES("APP_MOBILE_FIREBASE_ID", "AAAAEXE5AIg:APA91bFp6XhvY0Ic7xVyv8RjOIKoB0FeWkWVOU273GibYlD8q-RKRPWYm3A73DDtp3c1MqycrQFSpfF0dT7rMc7q5rt4n_hxdpFSEpwdgrAXDy7VepvCv6wBejSSmOh9xeQCIYom8ovb", "ID của app lái xe để call Firebase");

-- Create trigger tc_ve_update
CREATE 
	OR REPLACE TRIGGER tc_ve_update AFTER UPDATE ON tc_ve FOR EACH ROW
	UPDATE ban_ve_ve 
	SET bvv_lai_xe_don = new.lai_xe_id_don,
	bvv_lai_xe_tra = new.lai_xe_id_tra,
	bvv_trung_chuyen_a_order = new.thu_tu_don,
	bvv_trung_chuyen_b_order = new.thu_tu_tra,
	bvv_trung_chuyen_a_note = new.ghi_chu_don,
	bvv_trung_chuyen_b_note = new.ghi_chu_tra 
WHERE
	bvv_id = new.bvv_id;
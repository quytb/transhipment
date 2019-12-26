CREATE TABLE `db_phatlocan`.`tc_ctv_price_by_distance`
(
    `price_distance_id` INT    NOT NULL AUTO_INCREMENT,
    `distance`          DOUBLE NULL,
    `price`             DOUBLE NULL,
    `price_over`        DOUBLE NULL COMMENT 'Giá phụ trội/km',
    `price_id`          INT    NULL COMMENT 'FK: tc_ctv_price',
    PRIMARY KEY (`price_distance_id`)
);

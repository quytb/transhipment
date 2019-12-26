CREATE TABLE `dev_hasonhaivan`.`tc_ctv_price`
(
    `price_id` INT          NOT NULL AUTO_INCREMENT,
    `name`     VARCHAR(225) NULL,
    `type`     INT          NULL COMMENT '1: Giá mở cửa, 2 Giá cự li',
    PRIMARY KEY (`price_id`)
);

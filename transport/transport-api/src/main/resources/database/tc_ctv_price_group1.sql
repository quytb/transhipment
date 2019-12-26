CREATE TABLE `db_phatlocan`.`tc_ctv_price_group`
(
    `id`            INT NOT NULL AUTO_INCREMENT,
    `price_id`      INT NULL COMMENT 'FK:tc_ctv_price',
    `partner_id`    INT NULL COMMENT 'FK:tc_partner',
    PRIMARY KEY (`id`)
);

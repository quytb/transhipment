CREATE TABLE `db_phatlocan`.`tc_partner`
(
    `partner_id`     INT          NOT NULL AUTO_INCREMENT,
    `partner_name`   VARCHAR(255) NULL,
    `discount_range` DOUBLE       NULL,
    PRIMARY KEY (`partner_id`)
);

CREATE TABLE `db_phatlocan`.`tc_ctv_price_by_step`
(
    `price_step_id` INT    NOT NULL AUTO_INCREMENT,
    `distance_from` DOUBLE NULL,
    `distance_to`   DOUBLE NULL,
    `price`         DOUBLE NULL,
    `price_id`      INT    NULL,
    PRIMARY KEY (`price_step_id`)
);

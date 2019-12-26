package com.havaz.transport.dao.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "tc_notification_configure")
@Data
public class TcNotificationConfigureEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_configure_id")
    private Integer notificationConfigureId;

    @Column(name = "tc_bvv_id")
    private Integer bvvId;

    @Column(name = "notification_id")
    private Integer notificationId;
}

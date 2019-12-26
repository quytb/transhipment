package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tc_lenh_config")
@EntityListeners(AuditingEntityListener.class)
public class LenhConfigEntity implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "is_multiple", columnDefinition = "TINYINT")
    private int isMultiple;

    @LastModifiedDate
    @Column(name = "last_updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_updated_by")
    private int updatedBy;
}

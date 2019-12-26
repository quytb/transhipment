package com.havaz.transport.dao.entity;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.locationtech.jts.geom.Polygon;
import org.springframework.data.annotation.CreatedDate;
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
@Table(name = "tc_vung_trung_chuyen")
@EntityListeners(AuditingEntityListener.class)
public class TcVungTrungChuyenEntity implements java.io.Serializable {

    private static final long serialVersionUID = -5512191174047870815L;

    @Id
    @Column(name = "tc_vtt_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tcVttId;

    @Column(name = "tc_vtt_name")
    private String tcVttName;

    @Column(name = "tc_vtt_code")
    private String tcVttCode;

    @Column(name = "tc_vtt_content")
    private Polygon tcVttContent;

    @Column(name = "tc_vtt_locale")
    private String tcVttLocale;

    @Column(name = "tc_vtt_note")
    private String tcVttNote;

    @Column(name = "status", columnDefinition = "integer")
    private Boolean status;

    @Column(name = "tc_average_speed", columnDefinition = "FLOAT")
    private Double tcAverageSpeed;

    @Column(name = "tc_centroid_lat")
    private Double tcCentroidLat;

    @Column(name = "tc_centroid_long")
    private Double tcCentroidLong;

    @Column(name = "tc_confirmed_time")
    private Integer tcConfirmedTime;

    @Column(name = "created_by")
    private String createdBy;

    @CreatedDate
    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "last_updated_by")
    private String lastUpdatedBy;

    @LastModifiedDate
    @Column(name = "last_updated_date")
    private LocalDateTime lastUpdatedDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("tcVttId", tcVttId)
                .append("tcVttName", tcVttName)
                .append("tcVttCode", tcVttCode)
                .append("tcVttContent", tcVttContent)
                .append("tcVttLocale", tcVttLocale)
                .append("tcVttNote", tcVttNote)
                .append("status", status)
                .append("tcAverageSpeed", tcAverageSpeed)
                .append("tcCentroidLat", tcCentroidLat)
                .append("tcCentroidLong", tcCentroidLong)
                .append("tcConfirmedTime", tcConfirmedTime)
                .append("createdBy", createdBy)
                .append("createdDate", createdDate)
                .append("lastUpdatedBy", lastUpdatedBy)
                .append("lastUpdatedDate", lastUpdatedDate)
                .toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        TcVungTrungChuyenEntity that = (TcVungTrungChuyenEntity) o;

        return new EqualsBuilder()
                .append(tcVttId, that.tcVttId)
                .append(tcVttName, that.tcVttName)
                .append(tcVttCode, that.tcVttCode)
                .append(tcVttContent, that.tcVttContent)
                .append(createdBy, that.createdBy)
                .append(createdDate, that.createdDate)
                .append(lastUpdatedBy, that.lastUpdatedBy)
                .append(lastUpdatedDate, that.lastUpdatedDate)
                .append(tcVttLocale, that.tcVttLocale)
                .append(tcVttNote, that.tcVttNote)
                .append(status, that.status)
                .append(tcAverageSpeed, that.tcAverageSpeed)
                .append(tcCentroidLat, that.tcCentroidLat)
                .append(tcCentroidLong, that.tcCentroidLong)
                .append(tcConfirmedTime, that.tcConfirmedTime)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(tcVttId)
                .append(tcVttName)
                .append(tcVttCode)
                .append(tcVttContent)
                .append(createdBy)
                .append(createdDate)
                .append(lastUpdatedBy)
                .append(lastUpdatedDate)
                .append(tcVttLocale)
                .append(tcVttNote)
                .append(status)
                .append(tcAverageSpeed)
                .append(tcCentroidLat)
                .append(tcCentroidLong)
                .append(tcConfirmedTime)
                .toHashCode();
    }
}

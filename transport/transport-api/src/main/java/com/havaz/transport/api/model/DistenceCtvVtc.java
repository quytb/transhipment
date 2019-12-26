package com.havaz.transport.api.model;

import com.havaz.transport.dao.entity.TcVtcCtvEntity;

import java.util.Objects;

public class DistenceCtvVtc implements Comparable<DistenceCtvVtc> {
    private TcVtcCtvEntity tcVtcCtvEntity;
    private Double disctence;

    public TcVtcCtvEntity getTcVtcCtvEntity() {
        return tcVtcCtvEntity;
    }

    public void setTcVtcCtvEntity(TcVtcCtvEntity tcVtcCtvEntity) {
        this.tcVtcCtvEntity = tcVtcCtvEntity;
    }

    public Double getDisctence() {
        return disctence;
    }

    public void setDisctence(Double disctence) {
        this.disctence = disctence;
    }

    @Override
    public int compareTo(DistenceCtvVtc o) {
        if(this.getDisctence() > o.getDisctence()) {
            return 1;
        }
        else return -1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        DistenceCtvVtc distenceCtvVtc = (DistenceCtvVtc) obj;
        return  distenceCtvVtc.getTcVtcCtvEntity().getTcVttCtvId() == this.tcVtcCtvEntity.getTcVttCtvId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.tcVtcCtvEntity.getTcVttCtvId());
    }
}

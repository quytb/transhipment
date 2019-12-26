package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class XeTuyenDTO implements java.io.Serializable {

    private static final long serialVersionUID = 1L;

    private String bks;
    private String sdt;
    private Integer didId;
    private String machainId;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("bks", bks)
                .append("sdt", sdt)
                .append("didId", didId)
                .append("machainId", machainId)
                .toString();
    }
}

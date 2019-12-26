package com.havaz.transport.api.form;

import com.havaz.transport.api.model.TripStationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class XeTuyenTripStationForm {
    private String message;
    private List<TripStationDTO> data = new ArrayList<>();

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("message", message)
                .append("data", data)
                .toString();
    }
}

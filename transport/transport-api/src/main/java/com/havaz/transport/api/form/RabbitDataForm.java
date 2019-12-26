package com.havaz.transport.api.form;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class RabbitDataForm {

    @JsonProperty("occured_on")
    private String occuredOn;

    @JsonProperty("event_type")
    private String eventType;

    @JsonProperty("payload")
    private Object payload;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("occuredOn", occuredOn)
                .append("eventType", eventType)
                .append("payload", payload)
                .toString();
    }
}

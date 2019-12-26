package com.havaz.transport.api.rabbit.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.havaz.transport.api.form.VeActionForm;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

@Getter
@Setter
public class TransferTicketMessage extends Message {
    private static final long serialVersionUID = 1L;

    @JsonProperty(value = "merchant_id")
    private String merchantID;

    @JsonProperty(value = "content")
    private VeActionForm content;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("merchantID", merchantID)
                .append("content", content)
                .toString();
    }
}

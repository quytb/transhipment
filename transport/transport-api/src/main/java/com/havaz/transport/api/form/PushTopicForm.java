package com.havaz.transport.api.form;

import com.havaz.transport.api.model.LocationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PushTopicForm {
    private List<String> topics = new ArrayList<>();
    private Integer taiXeId;
    private String bks;
    private LocationDTO content;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.JSON_STYLE)
                .append("topics", topics)
                .append("taiXeId", taiXeId)
                .append("bks", bks)
                .append("content", content)
                .toString();
    }
}

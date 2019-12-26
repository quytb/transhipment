package com.havaz.transport.api.model;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString
public class NotificationManagerDTO implements Serializable {

    private Integer notificationId;

    private Boolean statusRead;

    private String content;

    private Integer type;

    private String mainUrl;

    private LocalDateTime createdDate;

    private LocalDateTime lastUpdatedDate;

    private List<Integer> bvvIds;


}

package com.havaz.transport.api.form;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class NotificationUpdateForm implements Serializable {
    private Integer notificationId;
    private Boolean statusRead;
}

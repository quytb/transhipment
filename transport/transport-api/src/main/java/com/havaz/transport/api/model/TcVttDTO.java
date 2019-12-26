package com.havaz.transport.api.model;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TcVttDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int tcVttId;
    private String tcVttName;
    private String tcVttCode;
    private String tcVttContent;
    private LocalDate createdDate;
    private String createdBy;
    private int status;
}

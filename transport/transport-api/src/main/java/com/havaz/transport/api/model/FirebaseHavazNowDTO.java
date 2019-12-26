package com.havaz.transport.api.model;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class FirebaseHavazNowDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String to;
    private Object data;
}

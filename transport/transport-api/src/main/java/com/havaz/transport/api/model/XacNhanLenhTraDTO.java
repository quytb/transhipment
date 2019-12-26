package com.havaz.transport.api.model;

import com.havaz.transport.api.form.location.Point;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class XacNhanLenhTraDTO implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    private int lenhId;
    private int taixeId;
    private Point toaDoTaiXe;
    private List<VeTcTraDTO> listVeTcUpdate = new ArrayList<>();
}

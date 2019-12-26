package com.havaz.transport.api.rabbit.publisher;

import com.havaz.transport.api.form.VeActionForm;
import com.havaz.transport.api.model.KhachHavazDTO;
import com.havaz.transport.api.model.NotifyLenhDTO;
import com.havaz.transport.api.model.TripDTO;
import com.havaz.transport.api.rabbit.message.TransferTicketMessage;

public interface RabbitMQPublisher {
    void sendMsgTrackingVehicle(TripDTO tripDTO);

    void sendMsgTrackingDriver(KhachHavazDTO khachDTO);

    void sendMsgDriverConfirmLenh(NotifyLenhDTO notifyLenhDTO);

    void sendTicketUpdate(VeActionForm message);
}

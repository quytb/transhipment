package com.havaz.transport.api.service;

import com.havaz.transport.api.body.request.tcve.GetTcVeDonRequest;
import com.havaz.transport.api.body.response.tcve.GetTcVeDonResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DieuhanhHubTrungchuyenDonService {
    Page<GetTcVeDonResponse> search(GetTcVeDonRequest request, Pageable pageable);
}

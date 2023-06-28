package com.dh.msusers.repository.feign;

import com.dh.msusers.model.Bill;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BillsFeignRepository implements IBillsFeignClient{

    private final IBillsFeignClient feignClient;


    @Override
    public List<Bill> getAll(String customerId) {
        List<Bill> response  = feignClient.getAll(customerId);
        return response;
    }
}

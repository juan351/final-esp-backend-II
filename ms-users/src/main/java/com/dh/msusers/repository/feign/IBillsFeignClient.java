package com.dh.msusers.repository.feign;

import com.dh.msusers.configuration.feign.FeignConfig;
import com.dh.msusers.model.Bill;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "ms-bill",configuration = FeignConfig.class)
public interface IBillsFeignClient {
    @GetMapping("/api/v1/bills/findById")
    List<Bill> getAll(@RequestParam String customerBill);
}

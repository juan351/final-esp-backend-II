package com.dh.msbills.controllers;

import com.dh.msbills.models.Bill;
import com.dh.msbills.services.BillService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bills")
@RequiredArgsConstructor
public class BillController {

    private final BillService service;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ROLE_app_user')")
    public ResponseEntity<List<Bill>> getAll() {
        return ResponseEntity.ok().body(service.getAllBill());
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('PROVIDERS')")
    public ResponseEntity<Bill> save(@RequestBody Bill bill){
        return ResponseEntity.ok().body(service.save(bill));
    }


    @GetMapping("/findById")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Bill>> getAll(@RequestParam String customerBill) {
        return ResponseEntity.ok().body(service.findByCustomerId(customerBill));
    }

}

package com.dh.msusers.service;

import com.dh.msusers.model.Bill;
import com.dh.msusers.model.User;
import com.dh.msusers.repository.KeycloakUserRepository;
import com.dh.msusers.repository.feign.BillsFeignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final KeycloakUserRepository userRepository;
    private final BillsFeignRepository billsFeignRepository;

    public User getAllBill(String customerId) {
        User usuario = userRepository.findById(customerId);
        usuario.setBills((List<Bill>) billsFeignRepository.getAll(customerId));
        return usuario;
    }
}

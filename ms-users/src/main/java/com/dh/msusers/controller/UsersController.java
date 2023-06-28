package com.dh.msusers.controller;

import com.dh.msusers.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UsersController {

    @Autowired
    UserService usersService;

    @GetMapping("/findById/{userId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getBillsByUserId (@PathVariable String userId) throws UsernameNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(usersService.getAllBill(userId));
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}

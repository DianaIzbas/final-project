package com.siit.finalproject.controller;


import com.siit.finalproject.entity.AccountEntity;
import com.siit.finalproject.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;


@RestController
@RequestMapping("/account")
public class AccountController {
    private AccountService service;

    public AccountController(AccountService service) throws IOException {
        this.service = service;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addAccount(@Valid @RequestBody AccountEntity account) throws IOException
    {

        try {
            service.addAccount(account);
            return new ResponseEntity<>("Account has been added: " , HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid account details", HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/password")
    public ResponseEntity<String> decryptedPassword() throws IOException
    {
        try {
            String password = service.decryptedPassword();
            return new ResponseEntity<>("Password is : " + password, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Invalid file", HttpStatus.BAD_REQUEST);
        }

    }
}

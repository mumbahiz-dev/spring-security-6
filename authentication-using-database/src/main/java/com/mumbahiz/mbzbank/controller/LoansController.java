package com.mumbahiz.mbzbank.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoansController {

    @GetMapping("/my-loans")
    public String getMyLoans(){
        return "Here are the loans details from the DB";
    }
}

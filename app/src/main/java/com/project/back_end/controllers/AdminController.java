package com.project.back_end.controllers;


import com.project.back_end.models.Admin;
import com.project.back_end.services.ValidationService;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("${api.path}admin")
public class AdminController {



    private final ValidationService validationService;





    /*
     * Constructor Injection
     */
    public AdminController(
            ValidationService validationService
    ) {

        this.validationService = validationService;

    }









    /*
     * Admin Login
     *
     * POST:
     * /api/admin/login
     *
     */
    @PostMapping("/login")
    public ResponseEntity<?> adminLogin(
            @RequestBody Admin admin
    ) {



        return validationService.validateAdmin(
                admin.getUsername(),
                admin.getPassword()
        );


    }



}
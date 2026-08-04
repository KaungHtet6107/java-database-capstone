package com.project.back_end.mvc;

import com.project.back_end.services.ValidationService;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {

    /*
     * Shared Validation Service
     */
    private final ValidationService validationService;



    /*
     * Constructor Injection
     */
    public DashboardController(
            ValidationService validationService
    ) {

        this.validationService = validationService;

    }






    /*
     * 1. Admin Dashboard
     *
     * GET:
     * /adminDashboard/{token}
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(
            @PathVariable String token
    ) {

        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "ADMIN"
                );



        if (validation.getStatusCode().is2xxSuccessful()) {

            return "admin/adminDashboard";

        }



        return "redirect:/";

    }






    /*
     * 2. Doctor Dashboard
     *
     * GET:
     * /doctorDashboard/{token}
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(
            @PathVariable String token
    ) {

        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "DOCTOR"
                );



        if (validation.getStatusCode().is2xxSuccessful()) {

            return "doctor/doctorDashboard";

        }



        return "redirect:/";

    }

}
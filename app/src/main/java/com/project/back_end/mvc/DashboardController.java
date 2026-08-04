package com.project.back_end.mvc;

import java.util.Map;

import com.project.back_end.services.ValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DashboardController {


    @Autowired
    private ValidationService validationService;


    /*
     * Admin Dashboard
     * URL:
     * http://localhost:8080/adminDashboard/{token}
     */
    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(
            @PathVariable String token) {


        Map<String, Object> validation =
                validationService.validateToken(token, "admin");


        if (validation.isEmpty()) {

            // Load:
            // src/main/resources/templates/admin/adminDashboard.html
            return "admin/adminDashboard";

        } else {

            // Invalid token
            return "redirect:/";
        }
    }



    /*
     * Doctor Dashboard
     * URL:
     * http://localhost:8080/doctorDashboard/{token}
     */
    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(
            @PathVariable String token) {


        Map<String, Object> validation =
                validationService.validateToken(token, "doctor");


        if (validation.isEmpty()) {

            // Load:
            // src/main/resources/templates/doctor/doctorDashboard.html
            return "doctor/doctorDashboard";

        } else {

            // Invalid token
            return "redirect:/";
        }
    }

}
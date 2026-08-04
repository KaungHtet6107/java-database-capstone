package com.project.back_end.controllers;


import com.project.back_end.models.Prescription;

import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.PrescriptionService;
import com.project.back_end.services.ValidationService;


import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;



import java.util.Map;



@RestController
@RequestMapping("${api.path}prescription")
public class PrescriptionController {



    private final PrescriptionService prescriptionService;

    private final ValidationService validationService;

    private final AppointmentService appointmentService;





    /*
     * Constructor Injection
     */
    public PrescriptionController(
            PrescriptionService prescriptionService,
            ValidationService validationService,
            AppointmentService appointmentService
    ) {

        this.prescriptionService = prescriptionService;

        this.validationService = validationService;

        this.appointmentService = appointmentService;

    }









    /*
     * Save Prescription
     *
     * POST:
     *
     * /prescription/save/{token}
     *
     */
    @PostMapping("/save/{token}")
    public ResponseEntity<?> savePrescription(
            @Valid
            @RequestBody Prescription prescription,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "doctor"
                );



        if(!validation.getStatusCode()
                .is2xxSuccessful()) {



            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired token"
                            )
                    );

        }







        /*
         * Update appointment status
         *
         * Status:
         * 0 = Scheduled
         * 1 = Completed
         * 2 = Prescription Added
         *
         */
        appointmentService.changeStatus(
                prescription.getAppointmentId(),
                2
        );








        return prescriptionService
                .savePrescription(
                        prescription
                );



    }









    /*
     * Get Prescription
     *
     * GET:
     *
     * /prescription/{appointmentId}/{token}
     *
     */
    @GetMapping("/{appointmentId}/{token}")
    public ResponseEntity<?> getPrescription(
            @PathVariable Long appointmentId,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "doctor"
                );



        if(!validation.getStatusCode()
                .is2xxSuccessful()) {



            return ResponseEntity
                    .status(
                            HttpStatus.UNAUTHORIZED
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid token"
                            )
                    );

        }







        return prescriptionService
                .getPrescription(
                        appointmentId
                );


    }



}
package com.project.back_end.controllers;


import com.project.back_end.DTO.Login;
import com.project.back_end.models.Patient;

import com.project.back_end.services.PatientService;
import com.project.back_end.services.ValidationService;


import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;



import java.util.Map;



@RestController
@RequestMapping("/patient")
public class PatientController {



    private final PatientService patientService;

    private final ValidationService validationService;





    /*
     * Constructor Injection
     */
    public PatientController(
            PatientService patientService,
            ValidationService validationService
    ) {

        this.patientService = patientService;

        this.validationService = validationService;

    }









    /*
     * Get Patient Details
     *
     * GET:
     *
     * /patient/{token}
     *
     */
    @GetMapping("/{token}")
    public ResponseEntity<?> getPatient(
            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "patient"
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





        Patient patient =
                patientService.getPatientDetails(token);

        if (patient == null) {

            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(
                            Map.of(
                                    "message",
                                    "Patient not found"
                            )
                    );
        }

        return ResponseEntity.ok(patient);


    }









    /*
     * Create Patient
     *
     * POST:
     *
     * /patient/create
     *
     */
    @PostMapping("/create")
    public ResponseEntity<?> createPatient(
            @Valid
            @RequestBody Patient patient
    ) {



        boolean valid =
                validationService.validatePatient(
                        patient.getEmail(),
                        patient.getPhone()
                );





        if(!valid) {


            return ResponseEntity
                    .status(
                            HttpStatus.CONFLICT
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Patient already exists"
                            )
                    );


        }






        int result =
                patientService
                        .createPatient(
                                patient
                        );






        if(result == 1) {


            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Patient created successfully"
                            )
                    );


        }





        return ResponseEntity
                .status(
                        HttpStatus.INTERNAL_SERVER_ERROR
                )
                .body(
                        Map.of(
                                "message",
                                "Failed to create patient"
                        )
                );


    }









    /*
     * Patient Login
     *
     * POST:
     *
     * /patient/login
     *
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid
            @RequestBody Login login
    ) {



        return validationService
                .validatePatientLogin(
                        login.getEmail(),
                        login.getPassword()
                );


    }









    /*
     * Get Patient Appointment
     *
     * GET:
     *
     * /patient/appointments/{patientId}/{user}/{token}
     *
     */
    @GetMapping("/appointments/{patientId}/{user}/{token}")
    public ResponseEntity<?> getPatientAppointment(
            @PathVariable Long patientId,

            @PathVariable String user,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        user
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






        return ResponseEntity.ok(
                patientService
                        .getPatientAppointment(
                                patientId
                        )
        );


    }









    /*
     * Filter Patient Appointment
     *
     * GET:
     *
     * /patient/filter/{condition}/{name}/{token}
     *
     */
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<?> filterPatientAppointment(
            @PathVariable String condition,

            @PathVariable String name,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "patient"
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






        return ResponseEntity.ok(
                validationService.filterPatient(
                        token,
                        condition,
                        name
                )
        );


    }



}
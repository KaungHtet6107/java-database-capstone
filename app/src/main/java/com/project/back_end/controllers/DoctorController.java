package com.project.back_end.controllers;


import com.project.back_end.DTO.Login;
import com.project.back_end.models.Doctor;

import com.project.back_end.services.DoctorService;
import com.project.back_end.services.ValidationService;


import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;



import java.util.HashMap;
import java.util.Map;



@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {



    private final DoctorService doctorService;

    private final ValidationService validationService;





    /*
     * Constructor Injection
     */
    public DoctorController(
            DoctorService doctorService,
            ValidationService validationService
    ) {

        this.doctorService = doctorService;

        this.validationService = validationService;

    }









    /*
     * Get Doctor Availability
     *
     * GET:
     *
     * /doctor/availability/{user}/{doctorId}/{date}/{token}
     *
     */
    @GetMapping("/availability/{user}/{doctorId}/{date}/{token}")
    public ResponseEntity<?> getDoctorAvailability(
            @PathVariable String user,
            @PathVariable Long doctorId,
            @PathVariable String date,
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
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid token"
                            )
                    );

        }





        return ResponseEntity.ok(
                doctorService.getDoctorAvailability(
                        doctorId,
                        java.time.LocalDate.parse(date)
                )
        );


    }









    /*
     * Get All Doctors
     *
     * GET:
     *
     * /doctor
     *
     */
    @GetMapping
    public ResponseEntity<?> getDoctor() {



        Map<String,Object> response =
                new HashMap<>();



        response.put(
                "doctors",
                doctorService.getDoctors()
        );



        return ResponseEntity
                .ok(response);

    }









    /*
     * Save Doctor
     *
     * POST:
     *
     * /doctor/save/{token}
     *
     */
    @PostMapping("/save/{token}")
    public ResponseEntity<?> saveDoctor(
            @Valid
            @RequestBody Doctor doctor,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "ADMIN"
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
                                    "Unauthorized"
                            )
                    );

        }






        int result =
                doctorService
                        .saveDoctor(
                                doctor
                        );





        if(result == -1) {


            return ResponseEntity
                    .status(
                            HttpStatus.CONFLICT
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor already exists"
                            )
                    );


        }





        if(result == 1) {


            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor saved successfully"
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
                                "Failed to save doctor"
                        )
                );


    }









    /*
     * Doctor Login
     *
     * POST:
     *
     * /doctor/login
     *
     */
    @PostMapping("/login")
    public ResponseEntity<?> doctorLogin(
            @Valid
            @RequestBody Login login
    ) {

        String result =
                doctorService.validateDoctor(
                        login.getEmail(),
                        login.getPassword()
                );

        if ("Doctor not found".equals(result) ||
                "Invalid password".equals(result)) {

            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    result
                            )
                    );
        }

        return ResponseEntity.ok(
                Map.of(
                        "token",
                        result,
                        "message",
                        "Login successful"
                )
        );
    }









    /*
     * Update Doctor
     *
     * PUT:
     *
     * /doctor/update/{token}
     *
     */
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateDoctor(
            @Valid
            @RequestBody Doctor doctor,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "ADMIN"
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
                                    "Unauthorized"
                            )
                    );

        }






        int result =
                doctorService.updateDoctor(
                        doctor.getId(),
                        doctor
                );





        if(result == -1) {


            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor not found"
                            )
                    );


        }





        if(result == 1) {


            return ResponseEntity
                    .ok(
                            Map.of(
                                    "message",
                                    "Doctor updated successfully"
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
                                "Update failed"
                        )
                );


    }









    /*
     * Delete Doctor
     *
     * DELETE:
     *
     * /doctor/delete/{id}/{token}
     *
     */
    @DeleteMapping("/delete/{id}/{token}")
    public ResponseEntity<?> deleteDoctor(
            @PathVariable Long id,

            @PathVariable String token
    ) {



        ResponseEntity<?> validation =
                validationService.validateToken(
                        token,
                        "ADMIN"
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
                                    "Unauthorized"
                            )
                    );

        }






        int result =
                doctorService
                        .deleteDoctor(
                                id
                        );





        if(result == -1) {


            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor not found"
                            )
                    );

        }





        if(result == 1) {


            return ResponseEntity
                    .ok(
                            Map.of(
                                    "message",
                                    "Doctor deleted successfully"
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
                                "Delete failed"
                        )
                );


    }









    /*
     * Filter Doctors
     *
     * GET:
     *
     * /doctor/filter/{name}/{time}/{speciality}
     *
     */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public ResponseEntity<?> filter(
            @PathVariable(required = false) String name,

            @PathVariable(required = false) String time,

            @PathVariable(required = false) String speciality
    ) {



        Map<String,Object> response =
                new HashMap<>();



        response.put(
                "doctors",
                validationService.filterDoctor(
                        name,
                        speciality,
                        time
                )
        );



        return ResponseEntity
                .ok(response);


    }



}
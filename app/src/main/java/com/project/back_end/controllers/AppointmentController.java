package com.project.back_end.controllers;


import com.project.back_end.models.Appointment;

import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.ValidationService;


import jakarta.validation.Valid;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import org.springframework.web.bind.annotation.*;


import java.util.Map;



@RestController
@RequestMapping("/appointments")
public class AppointmentController {



    private final AppointmentService appointmentService;

    private final ValidationService validationService;





    /*
     * Constructor Injection
     */
    public AppointmentController(
            AppointmentService appointmentService,
            ValidationService validationService
    ) {

        this.appointmentService = appointmentService;

        this.validationService = validationService;

    }









    /*
     * Get Appointments
     *
     * GET:
     *
     * /appointments/{date}/{patientName}/{token}
     *
     */
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<?> getAppointments(
            @PathVariable String date,
            @PathVariable String patientName,
            @PathVariable String token
    ) {



        if(!validationService.validateToken(
                token,
                "DOCTOR"
        ).getStatusCode().is2xxSuccessful()) {



            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired token"
                            )
                    );

        }




        String doctorEmail =
                validationService.getEmailFromToken(token);

        Long doctorId =
                validationService.getDoctorId(doctorEmail);

        return ResponseEntity.ok(
                appointmentService.getAppointments(
                        doctorId,
                        java.time.LocalDate.parse(date),
                        patientName
                )
        );


    }









    /*
     * Book Appointment
     *
     * POST:
     *
     * /appointments/book/{token}
     *
     */
    @PostMapping("/book/{token}")
    public ResponseEntity<?> bookAppointment(
            @Valid
            @RequestBody Appointment appointment,

            @PathVariable String token
    ) {



        if(!validationService.validateToken(
                token,
                "PATIENT"
        ).getStatusCode().is2xxSuccessful()) {



            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired token"
                            )
                    );

        }






        int validateResult =
                validationService.validateAppointment(
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentTime()
                );





        if(validateResult == -1) {


            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor not found"
                            )
                    );


        }





        if(validateResult == 0) {


            return ResponseEntity
                    .badRequest()
                    .body(
                            Map.of(
                                    "message",
                                    "Doctor is not available at this time"
                            )
                    );


        }






        int result =
                appointmentService
                        .bookAppointment(
                                appointment
                        );





        if(result == 1) {


            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(
                            Map.of(
                                    "message",
                                    "Appointment booked successfully"
                            )
                    );

        }





        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        Map.of(
                                "message",
                                "Failed to book appointment"
                        )
                );


    }









    /*
     * Update Appointment
     *
     * PUT:
     *
     * /appointments/update/{token}
     *
     */
    @PutMapping("/update/{token}")
    public ResponseEntity<?> updateAppointment(
            @Valid
            @RequestBody Appointment appointment,

            @PathVariable String token
    ) {



        if(!validationService.validateToken(
                token,
                "PATIENT"
        ).getStatusCode().is2xxSuccessful()) {



            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired token"
                            )
                    );

        }






        String email =
                validationService.getEmailFromToken(token);

        Long patientId =
                validationService.getPatientId(email);

        String result =
                appointmentService.updateAppointment(
                        appointment.getId(),
                        patientId,
                        appointment.getAppointmentTime()
                );





        if(result.equals("Appointment updated successfully")) {



            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Appointment updated successfully"
                    )
            );


        }





        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "message",
                                result
                        )
                );


    }









    /*
     * Cancel Appointment
     *
     * DELETE:
     *
     * /appointments/cancel/{id}/{token}
     *
     */
    @DeleteMapping("/cancel/{id}/{token}")
    public ResponseEntity<?> cancelAppointment(
            @PathVariable Long id,

            @PathVariable String token
    ) {



        if(!validationService.validateToken(
                token,
                "PATIENT"
        ).getStatusCode().is2xxSuccessful()) {



            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(
                            Map.of(
                                    "message",
                                    "Invalid or expired token"
                            )
                    );

        }






        String email =
                validationService.getEmailFromToken(token);

        Long patientId =
                validationService.getPatientId(email);

        String result =
                appointmentService.cancelAppointment(
                        id,
                        patientId
                );





        if(result.equals("Appointment cancelled successfully")) {



            return ResponseEntity.ok(
                    Map.of(
                            "message",
                            "Appointment cancelled successfully"
                    )
            );


        }





        return ResponseEntity
                .badRequest()
                .body(
                        Map.of(
                                "message",
                                result
                        )
                );


    }



}
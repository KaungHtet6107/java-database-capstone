package com.project.back_end.services;

import com.project.back_end.models.Prescription;
import com.project.back_end.repo.PrescriptionRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



@Service
public class PrescriptionService {


    private final PrescriptionRepository prescriptionRepository;


    private static final Logger logger =
            LoggerFactory.getLogger(PrescriptionService.class);



    /*
     * Constructor Injection
     */
    public PrescriptionService(
            PrescriptionRepository prescriptionRepository
    ) {

        this.prescriptionRepository = prescriptionRepository;

    }






    /*
     * Save Prescription
     *
     * Response:
     *
     * 400 -> Prescription already exists
     * 201 -> Created successfully
     * 500 -> Server error
     */
    public ResponseEntity<Map<String, Object>> savePrescription(
            Prescription prescription
    ) {


        Map<String, Object> response =
                new HashMap<>();


        try {


            /*
             * Check existing prescription
             * using appointment ID
             */
            List<Prescription> existingPrescriptions =
                    prescriptionRepository
                            .findByAppointmentId(
                                    prescription.getAppointmentId()
                            );



            if(!existingPrescriptions.isEmpty()) {


                response.put(
                        "message",
                        "Prescription already exists for this appointment"
                );


                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(response);

            }




            prescriptionRepository.save(
                    prescription
            );



            response.put(
                    "message",
                    "Prescription created successfully"
            );


            response.put(
                    "prescription",
                    prescription
            );



            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(response);




        } catch(Exception e) {


            logger.error(
                    "Error while saving prescription",
                    e
            );



            response.put(
                    "message",
                    "Internal server error"
            );


            response.put(
                    "error",
                    e.getMessage()
            );



            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(response);

        }

    }








    /*
     * Get Prescription By Appointment ID
     *
     * Response:
     *
     * 200 -> Found
     * 500 -> Error
     */
    public ResponseEntity<Map<String, Object>> getPrescription(
            Long appointmentId
    ) {


        Map<String, Object> response =
                new HashMap<>();



        try {


            List<Prescription> prescriptions =
                    prescriptionRepository
                            .findByAppointmentId(
                                    appointmentId
                            );



            if(prescriptions.isEmpty()) {


                response.put(
                        "message",
                        "No prescription found for this appointment"
                );


                return ResponseEntity
                        .status(HttpStatus.OK)
                        .body(response);

            }





            response.put(
                    "message",
                    "Prescription found"
            );


            response.put(
                    "prescription",
                    prescriptions
            );



            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(response);




        } catch(Exception e) {


            logger.error(
                    "Error while fetching prescription",
                    e
            );



            response.put(
                    "message",
                    "Internal server error"
            );


            response.put(
                    "error",
                    e.getMessage()
            );



            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(response);

        }

    }


}
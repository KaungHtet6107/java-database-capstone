package com.project.back_end.services;


import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import com.project.back_end.repo.AppointmentRepository;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ValidationService {


    private final TokenService tokenService;


    private final AdminRepository adminRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;


    private final DoctorService doctorService;

    private final PatientService patientService;



    private static final Logger logger =
            LoggerFactory.getLogger(ValidationService.class);





    /*
     * Constructor Injection
     */
    public ValidationService(
            TokenService tokenService,
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            DoctorService doctorService,
            PatientService patientService
    ) {


        this.tokenService = tokenService;

        this.adminRepository = adminRepository;

        this.doctorRepository = doctorRepository;

        this.patientRepository = patientRepository;

        this.appointmentRepository = appointmentRepository;

        this.doctorService = doctorService;

        this.patientService = patientService;

    }







    /*
     * 1. Validate JWT Token By Role
     */
    public ResponseEntity<?> validateToken(
            String token,
            String role
    ) {

        Map<String, Object> response = new HashMap<>();

        try {

            boolean valid =
                    tokenService.validateToken(
                            token,
                            role
                    );

            if (!valid) {

                response.put(
                        "message",
                        "Invalid or expired token"
                );

                return ResponseEntity
                        .status(HttpStatus.UNAUTHORIZED)
                        .body(response);
            }

            response.put(
                    "message",
                    "Token is valid"
            );

            return ResponseEntity.ok(response);

        } catch (Exception e) {

            logger.error(
                    "Token validation error",
                    e
            );

            response.put(
                    "message",
                    "Internal server error"
            );

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(response);
        }

    }








    /*
     * 2. Validate Admin Login
     */
    public ResponseEntity<?> validateAdmin(
            String username,
            String password
    ) {


        Map<String,Object> response =
                new HashMap<>();


        try {


            Admin admin =
                    adminRepository
                            .findByUsername(username);



            if(admin == null) {


                response.put(
                        "message",
                        "Admin not found"
                );


                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);

            }




            if(!admin.getPassword()
                    .equals(password)) {


                response.put(
                        "message",
                        "Invalid password"
                );


                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);

            }




            String token =
                    tokenService
                            .generateToken(
                                    admin.getUsername(),
                                    "ADMIN"
                            );



            response.put(
                    "token",
                    token
            );


            response.put(
                    "message",
                    "Login successful"
            );



            return ResponseEntity
                    .ok(response);




        } catch(Exception e) {


            logger.error(
                    "Admin login error",
                    e
            );


            response.put(
                    "message",
                    "Internal server error"
            );


            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(response);

        }

    }









    /*
     * 3. Filter Doctor
     */
    public List<Doctor> filterDoctor(
            String name,
            String specialty,
            String availableTime
    ) {



        if(name == null &&
                specialty == null &&
                availableTime == null) {


            return doctorRepository.findAll();

        }





        List<Doctor> doctors;



        if(name != null &&
                specialty != null) {


            doctors =
                    doctorRepository
                            .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                                    name,
                                    specialty
                            );


        } else if(name != null) {


            doctors =
                    doctorRepository
                            .findByNameLike(
                                    "%" + name + "%"
                            );


        } else if(specialty != null) {


            doctors =
                    doctorRepository
                            .findBySpecialtyIgnoreCase(
                                    specialty
                            );


        } else {


            doctors =
                    doctorRepository.findAll();

        }



        if(availableTime != null) {


            doctors =
                    doctorService
                            .filterDoctorByTime(
                                    doctors,
                                    availableTime
                            );

        }



        return doctors;

    }









    /*
     * 4. Validate Appointment
     */
    public int validateAppointment(
            Long doctorId,
            LocalDateTime appointmentTime
    ) {


        Doctor doctor =
                doctorRepository
                        .findById(doctorId)
                        .orElse(null);



        if(doctor == null) {

            return -1;

        }



        String requestedTime =
                appointmentTime
                        .toLocalTime()
                        .toString();



        if(doctor.getAvailableTimes()
                .contains(requestedTime)) {


            return 1;

        }



        return 0;

    }









    /*
     * 5. Validate Patient Registration
     */
    public boolean validatePatient(
            String email,
            String phone
    ) {


        Patient patient =
                patientRepository
                        .findByEmailOrPhone(
                                email,
                                phone
                        );



        return patient == null;

    }









    /*
     * 6. Validate Patient Login
     */
    public ResponseEntity<?> validatePatientLogin(
            String email,
            String password
    ) {


        Map<String,Object> response =
                new HashMap<>();



        try {


            Patient patient =
                    patientRepository
                            .findByEmail(email);



            if(patient == null) {


                response.put(
                        "message",
                        "Patient not found"
                );


                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);

            }




            if(!patient.getPassword()
                    .equals(password)) {


                response.put(
                        "message",
                        "Invalid password"
                );


                return ResponseEntity
                        .status(
                                HttpStatus.UNAUTHORIZED
                        )
                        .body(response);

            }




            String token =
                    tokenService
                            .generateToken(
                                    patient.getEmail(),
                                    "PATIENT"
                            );



            response.put(
                    "token",
                    token
            );


            response.put(
                    "message",
                    "Login successful"
            );



            return ResponseEntity
                    .ok(response);




        } catch(Exception e) {


            logger.error(
                    "Patient login error",
                    e
            );


            response.put(
                    "message",
                    "Internal server error"
            );


            return ResponseEntity
                    .status(
                            HttpStatus.INTERNAL_SERVER_ERROR
                    )
                    .body(response);

        }

    }









    /*
     * 7. Filter Patient Appointment
     */
    public Object filterPatient(
            String token,
            String condition,
            String doctorName
    ) {


        try {


            String email =
                    tokenService
                            .getEmailFromToken(token);



            Patient patient =
                    patientRepository
                            .findByEmail(email);



            if(patient == null) {


                return "Patient not found";

            }




            Long patientId =
                    patient.getId();




            if(condition != null &&
                    doctorName != null) {


                return patientService
                        .filterByDoctorAndCondition(
                                doctorName,
                                patientId,
                                condition
                        );


            } else if(condition != null) {


                return patientService
                        .filterByCondition(
                                patientId,
                                condition
                        );


            } else if(doctorName != null) {


                return patientService
                        .filterByDoctor(
                                doctorName,
                                patientId
                        );

            }




            return patientService
                    .getPatientAppointment(
                            patientId
                    );



        } catch(Exception e) {


            logger.error(
                    "Patient filtering error",
                    e
            );


            return "Internal server error";

        }

    }



    /*
     * 8. 3 Helper Methods
     */
    public String getEmailFromToken(
            String token
    ) {

        return tokenService.getEmailFromToken(token);

    }


    public Long getDoctorId(
            String email
    ) {

        Doctor doctor =
                doctorRepository.findByEmail(email);

        if (doctor == null) {

            return null;

        }

        return doctor.getId();

    }

    public Long getPatientId(
            String email
    ) {

        Patient patient =
                patientRepository.findByEmail(email);

        if (patient == null) {

            return null;

        }

        return patient.getId();

    }

}
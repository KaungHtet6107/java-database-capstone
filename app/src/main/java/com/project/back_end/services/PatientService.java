package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class PatientService {


    private final PatientRepository patientRepository;

    private final AppointmentRepository appointmentRepository;

    private final TokenService tokenService;


    private static final Logger logger =
            LoggerFactory.getLogger(PatientService.class);



    /*
     * Constructor Injection
     */
    public PatientService(
            PatientRepository patientRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {

        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;

    }






    /*
     * 1. Create Patient
     *
     * Return:
     * 1 = success
     * 0 = failed
     */
    public int createPatient(
            Patient patient
    ) {


        try {


            patientRepository.save(patient);


            return 1;



        } catch(Exception e) {


            logger.error(
                    "Failed to create patient",
                    e
            );


            return 0;

        }

    }







    /*
     * 2. Get Patient Appointments
     *
     * Convert Appointment Entity
     * to AppointmentDTO
     */
    @Transactional
    public List<AppointmentDTO> getPatientAppointment(
            Long patientId
    ) {


        try {


            List<Appointment> appointments =
                    appointmentRepository
                            .findByPatientId(
                                    patientId
                            );



            return appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());



        } catch(Exception e) {


            logger.error(
                    "Failed to get patient appointments",
                    e
            );


            return List.of();

        }

    }








    /*
     * 3. Filter Appointment By Condition
     *
     * condition:
     *
     * future -> status 0
     * past   -> status 1
     */
    @Transactional
    public List<AppointmentDTO> filterByCondition(
            Long patientId,
            String condition
    ) {


        try {


            int status;



            if(condition.equalsIgnoreCase("future")) {


                status = 0;


            } else if(condition.equalsIgnoreCase("past")) {


                status = 1;


            } else {


                throw new IllegalArgumentException(
                        "Invalid condition"
                );

            }



            List<Appointment> appointments =
                    appointmentRepository
                            .findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
                                    patientId,
                                    status
                            );



            return appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());



        } catch(Exception e) {


            logger.error(
                    "Failed to filter appointments by condition",
                    e
            );


            return List.of();

        }

    }









    /*
     * 4. Filter Appointment By Doctor Name
     */
    @Transactional
    public List<AppointmentDTO> filterByDoctor(
            String doctorName,
            Long patientId
    ) {


        try {


            List<Appointment> appointments =
                    appointmentRepository
                            .filterByDoctorNameAndPatientId(
                                    doctorName,
                                    patientId
                            );



            return appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());



        } catch(Exception e) {


            logger.error(
                    "Failed to filter appointments by doctor",
                    e
            );


            return List.of();

        }

    }









    /*
     * 5. Filter Doctor + Condition
     */
    @Transactional
    public List<AppointmentDTO> filterByDoctorAndCondition(
            String doctorName,
            Long patientId,
            String condition
    ) {


        try {


            int status;



            if(condition.equalsIgnoreCase("future")) {


                status = 0;


            } else if(condition.equalsIgnoreCase("past")) {


                status = 1;


            } else {


                throw new IllegalArgumentException(
                        "Invalid condition"
                );

            }




            List<Appointment> appointments =
                    appointmentRepository
                            .filterByDoctorNameAndPatientIdAndStatus(
                                    doctorName,
                                    patientId,
                                    status
                            );



            return appointments.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList());



        } catch(Exception e) {


            logger.error(
                    "Failed to filter doctor and condition",
                    e
            );


            return List.of();

        }

    }








    /*
     * 6. Get Patient Details
     *
     * Extract email from JWT token
     */
    @Transactional
    public Patient getPatientDetails(
            String token
    ) {


        try {


            String email =
                    tokenService
                            .getEmailFromToken(token);



            return patientRepository
                    .findByEmail(email);



        } catch(Exception e) {


            logger.error(
                    "Failed to get patient details",
                    e
            );


            return null;

        }

    }







    /*
     * Convert Appointment Entity
     * into AppointmentDTO
     */
    private AppointmentDTO convertToDTO(
            Appointment appointment
    ) {


        return new AppointmentDTO(

                appointment.getId(),

                appointment.getDoctor().getId(),

                appointment.getDoctor().getName(),


                appointment.getPatient().getId(),

                appointment.getPatient().getName(),

                appointment.getPatient().getEmail(),

                appointment.getPatient().getPhone(),

                appointment.getPatient().getAddress(),


                appointment.getAppointmentTime(),

                appointment.getStatus()

        );

    }


}
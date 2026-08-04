package com.project.back_end.services;

import com.project.back_end.DTO.AppointmentDTO;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.repo.AppointmentRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class AppointmentService {


    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final TokenService tokenService;



    /*
     * Constructor Injection
     */
    public AppointmentService(
            AppointmentRepository appointmentRepository,
            PatientRepository patientRepository,
            DoctorRepository doctorRepository,
            TokenService tokenService
    ) {

        this.appointmentRepository = appointmentRepository;
        this.patientRepository = patientRepository;
        this.doctorRepository = doctorRepository;
        this.tokenService = tokenService;
    }



    /*
     * 1. Book Appointment
     *
     * Return:
     * 1 -> success
     * 0 -> failed
     */
    @Transactional
    public int bookAppointment(Appointment appointment) {

        try {

            appointmentRepository.save(appointment);

            return 1;

        } catch (Exception e) {

            return 0;
        }
    }





    /*
     * 2. Update Appointment
     */
    @Transactional
    public String updateAppointment(
            Long appointmentId,
            Long patientId,
            LocalDateTime newAppointmentTime
    ) {


        Optional<Appointment> optionalAppointment =
                appointmentRepository.findById(appointmentId);


        if(optionalAppointment.isEmpty()) {

            return "Appointment not found";
        }


        Appointment appointment =
                optionalAppointment.get();



        /*
         * Check patient ownership
         */
        if(!appointment.getPatient()
                .getId()
                .equals(patientId)) {


            return "You cannot update this appointment";
        }



        /*
         * Check doctor availability
         */
        Doctor doctor = appointment.getDoctor();


        String requestedTime =
                newAppointmentTime.toLocalTime()
                        .toString();



        if(!doctor.getAvailableTimes()
                .contains(requestedTime)) {


            return "Doctor is not available at this time";
        }



        appointment.setAppointmentTime(
                newAppointmentTime
        );


        appointmentRepository.save(
                appointment
        );


        return "Appointment updated successfully";

    }





    /*
     * 3. Cancel Appointment
     */
    @Transactional
    public String cancelAppointment(
            Long appointmentId,
            Long patientId
    ) {


        Optional<Appointment> optionalAppointment =
                appointmentRepository.findById(
                        appointmentId
                );


        if(optionalAppointment.isEmpty()) {

            return "Appointment not found";
        }



        Appointment appointment =
                optionalAppointment.get();



        /*
         * Verify patient ownership
         */
        if(!appointment.getPatient()
                .getId()
                .equals(patientId)) {


            return "You cannot cancel this appointment";
        }



        appointmentRepository.delete(
                appointment
        );


        return "Appointment cancelled successfully";

    }





    /*
     * 4. Get Appointments
     *
     * Doctor appointments by date
     * Optional patient name filter
     */
    @Transactional
    public List<AppointmentDTO> getAppointments(
            Long doctorId,
            LocalDate date,
            String patientName
    ) {


        LocalDateTime start =
                date.atStartOfDay();


        LocalDateTime end =
                date.atTime(
                        LocalTime.MAX
                );


        List<Appointment> appointments;



        if(patientName == null ||
                patientName.isEmpty()) {


            appointments =
                    appointmentRepository
                            .findByDoctorIdAndAppointmentTimeBetween(
                                    doctorId,
                                    start,
                                    end
                            );


        } else {


            appointments =
                    appointmentRepository
                            .findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
                                    doctorId,
                                    patientName,
                                    start,
                                    end
                            );
        }



        return appointments.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

    }





    /*
     * 5. Change Appointment Status
     */
    @Transactional
    public void changeStatus(
            Long appointmentId,
            int status
    ) {


        appointmentRepository.updateStatus(
                status,
                appointmentId
        );

    }







    /*
     * Convert Entity -> DTO
     */
    private AppointmentDTO convertToDTO(
            Appointment appointment
    ) {


        Doctor doctor =
                appointment.getDoctor();


        Patient patient =
                appointment.getPatient();



        return new AppointmentDTO(

                appointment.getId(),

                doctor.getId(),
                doctor.getName(),

                patient.getId(),
                patient.getName(),
                patient.getEmail(),
                patient.getPhone(),
                patient.getAddress(),

                appointment.getAppointmentTime(),

                appointment.getStatus()
        );

    }

}
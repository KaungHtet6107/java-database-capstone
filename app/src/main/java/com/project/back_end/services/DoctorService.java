package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.AppointmentRepository;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class DoctorService {


    private final DoctorRepository doctorRepository;

    private final AppointmentRepository appointmentRepository;

    private final TokenService tokenService;



    /*
     * Constructor Injection
     */
    public DoctorService(
            DoctorRepository doctorRepository,
            AppointmentRepository appointmentRepository,
            TokenService tokenService
    ) {

        this.doctorRepository = doctorRepository;
        this.appointmentRepository = appointmentRepository;
        this.tokenService = tokenService;

    }





    /*
     * 1. Get Doctor Availability
     *
     * Returns available time slots
     * excluding booked appointments.
     */
    @Transactional
    public List<String> getDoctorAvailability(
            Long doctorId,
            LocalDate date
    ) {


        Doctor doctor =
                doctorRepository
                        .findById(doctorId)
                        .orElse(null);


        if(doctor == null) {

            return List.of();
        }



        LocalDateTime start =
                date.atStartOfDay();


        LocalDateTime end =
                date.atTime(LocalTime.MAX);



        List<Appointment> appointments =
                appointmentRepository
                        .findByDoctorIdAndAppointmentTimeBetween(
                                doctorId,
                                start,
                                end
                        );



        List<String> bookedTimes =
                appointments.stream()
                        .map(a ->
                                a.getAppointmentTime()
                                        .toLocalTime()
                                        .toString()
                        )
                        .collect(Collectors.toList());



        return doctor.getAvailableTimes()
                .stream()
                .filter(time ->
                        !bookedTimes.contains(time)
                )
                .collect(Collectors.toList());

    }





    /*
     * 2. Save Doctor
     *
     * Return:
     *  1 success
     *  0 error
     * -1 duplicate email
     */
    @Transactional
    public int saveDoctor(Doctor doctor) {


        try {


            Doctor existing =
                    doctorRepository
                            .findByEmail(
                                    doctor.getEmail()
                            );


            if(existing != null) {

                return -1;
            }


            doctorRepository.save(doctor);


            return 1;


        } catch(Exception e) {


            return 0;

        }

    }






    /*
     * 3. Update Doctor
     */
    @Transactional
    public int updateDoctor(
            Long id,
            Doctor doctor
    ) {


        try {


            Optional<Doctor> existing =
                    doctorRepository.findById(id);



            if(existing.isEmpty()) {

                return -1;
            }



            doctor.setId(id);


            doctorRepository.save(doctor);


            return 1;



        } catch(Exception e) {


            return 0;
        }

    }







    /*
     * 4. Get All Doctors
     */
    @Transactional
    public List<Doctor> getDoctors() {


        return doctorRepository.findAll();

    }







    /*
     * 5. Delete Doctor
     */
    @Transactional
    public int deleteDoctor(
            Long doctorId
    ) {


        try {


            Optional<Doctor> doctor =
                    doctorRepository
                            .findById(doctorId);



            if(doctor.isEmpty()) {

                return -1;
            }



            appointmentRepository
                    .deleteAllByDoctorId(
                            doctorId
                    );



            doctorRepository.delete(
                    doctor.get()
            );



            return 1;



        } catch(Exception e) {


            return 0;

        }

    }







    /*
     * 6. Validate Doctor Login
     */
    @Transactional
    public String validateDoctor(
            String email,
            String password
    ) {


        Doctor doctor =
                doctorRepository
                        .findByEmail(email);



        if(doctor == null) {

            return "Doctor not found";
        }



        if(!doctor.getPassword()
                .equals(password)) {


            return "Invalid password";

        }



        return tokenService
                .generateToken(
                        doctor.getEmail(),
                        "DOCTOR"
                );

    }







    /*
     * 7. Find Doctor By Name
     */
    @Transactional
    public List<Doctor> findDoctorByName(
            String name
    ) {


        return doctorRepository
                .findByNameLike(
                        "%" + name + "%"
                );

    }








    /*
     * 8. Filter Name + Specialty + Time
     */
    @Transactional
    public List<Doctor> filterDoctorsByNameSpecilityandTime(
            String name,
            String specialty,
            String time
    ) {


        List<Doctor> doctors =
                doctorRepository
                        .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                                name,
                                specialty
                        );



        return filterDoctorByTime(
                doctors,
                time
        );

    }








    /*
     * 9. Filter Doctor By Time
     */
    @Transactional
    public List<Doctor> filterDoctorByTime(
            List<Doctor> doctors,
            String period
    ) {


        return doctors.stream()
                .filter(
                        doctor ->
                                doctor.getAvailableTimes()
                                        .stream()
                                        .anyMatch(time -> {


                                            LocalTime localTime =
                                                    LocalTime.parse(time);


                                            if(period.equalsIgnoreCase("AM")) {

                                                return localTime
                                                        .isBefore(
                                                                LocalTime.NOON
                                                        );
                                            }


                                            if(period.equalsIgnoreCase("PM")) {

                                                return localTime
                                                        .isAfter(
                                                                LocalTime.NOON
                                                        );
                                            }


                                            return false;


                                        })
                )
                .collect(Collectors.toList());

    }







    /*
     * 10. Filter Name + Time
     */
    @Transactional
    public List<Doctor> filterDoctorByNameAndTime(
            String name,
            String time
    ) {


        List<Doctor> doctors =
                doctorRepository
                        .findByNameLike(
                                "%" + name + "%"
                        );



        return filterDoctorByTime(
                doctors,
                time
        );

    }








    /*
     * 11. Filter Name + Specialty
     */
    @Transactional
    public List<Doctor> filterDoctorByNameAndSpecility(
            String name,
            String specialty
    ) {


        return doctorRepository
                .findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
                        name,
                        specialty
                );

    }







    /*
     * 12. Filter Time + Specialty
     */
    @Transactional
    public List<Doctor> filterDoctorByTimeAndSpecility(
            String specialty,
            String time
    ) {


        List<Doctor> doctors =
                doctorRepository
                        .findBySpecialtyIgnoreCase(
                                specialty
                        );



        return filterDoctorByTime(
                doctors,
                time
        );

    }







    /*
     * 13. Filter Specialty Only
     */
    @Transactional
    public List<Doctor> filterDoctorBySpecility(
            String specialty
    ) {


        return doctorRepository
                .findBySpecialtyIgnoreCase(
                        specialty
                );

    }







    /*
     * 14. Filter All Doctors By Time
     */
    @Transactional
    public List<Doctor> filterDoctorsByTime(
            String time
    ) {


        List<Doctor> doctors =
                doctorRepository.findAll();



        return filterDoctorByTime(
                doctors,
                time
        );

    }

}
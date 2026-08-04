package com.project.back_end.repo;

import com.project.back_end.models.Appointment;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

    /**
     * Find appointments for a doctor within a specific time range.
     * Fetches the doctor's available times eagerly.
     */
    @Query("""
            SELECT DISTINCT a
            FROM Appointment a
            LEFT JOIN FETCH a.doctor d
            LEFT JOIN FETCH d.availableTimes
            WHERE d.id = :doctorId
              AND a.appointmentTime BETWEEN :start AND :end
            """)
    List<Appointment> findByDoctorIdAndAppointmentTimeBetween(
            Long doctorId,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Find appointments for a doctor by patient name within a time range.
     * Fetches both doctor and patient information eagerly.
     */
    @Query("""
            SELECT DISTINCT a
            FROM Appointment a
            LEFT JOIN FETCH a.doctor d
            LEFT JOIN FETCH d.availableTimes
            LEFT JOIN FETCH a.patient p
            WHERE d.id = :doctorId
              AND LOWER(p.name) LIKE LOWER(CONCAT('%', :patientName, '%'))
              AND a.appointmentTime BETWEEN :start AND :end
            """)
    List<Appointment> findByDoctorIdAndPatient_NameContainingIgnoreCaseAndAppointmentTimeBetween(
            Long doctorId,
            String patientName,
            LocalDateTime start,
            LocalDateTime end
    );

    /**
     * Delete all appointments for a specific doctor.
     */
    @Modifying
    @Transactional
    void deleteAllByDoctorId(Long doctorId);

    /**
     * Find all appointments for a patient.
     */
    List<Appointment> findByPatientId(Long patientId);

    /**
     * Find appointments for a patient by status,
     * ordered by appointment time.
     */
    List<Appointment> findByPatient_IdAndStatusOrderByAppointmentTimeAsc(
            Long patientId,
            int status
    );

    /**
     * Filter appointments by doctor name and patient ID.
     */
    @Query("""
            SELECT a
            FROM Appointment a
            WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
              AND a.patient.id = :patientId
            """)
    List<Appointment> filterByDoctorNameAndPatientId(
            String doctorName,
            Long patientId
    );

    /**
     * Filter appointments by doctor name,
     * patient ID, and status.
     */
    @Query("""
            SELECT a
            FROM Appointment a
            WHERE LOWER(a.doctor.name) LIKE LOWER(CONCAT('%', :doctorName, '%'))
              AND a.patient.id = :patientId
              AND a.status = :status
            """)
    List<Appointment> filterByDoctorNameAndPatientIdAndStatus(
            String doctorName,
            Long patientId,
            int status
    );

    /**
     * Update appointment status.
     */
    @Modifying
    @Transactional
    @Query("""
            UPDATE Appointment a
            SET a.status = :status
            WHERE a.id = :id
            """)
    void updateStatus(
            int status,
            long id
    );

}
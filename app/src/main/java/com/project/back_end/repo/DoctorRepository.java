package com.project.back_end.repo;

import com.project.back_end.models.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /**
     * Find a doctor by email.
     *
     * @param email the doctor's email
     * @return the matching Doctor, or null if not found
     */
    Doctor findByEmail(String email);

    /**
     * Find doctors whose name contains the given text.
     * (Case-sensitive depending on the database collation.)
     *
     * @param name the name to search for
     * @return list of matching doctors
     */
    List<Doctor> findByNameLike(String name);

    /**
     * Find doctors by name (contains, ignoring case)
     * and specialty (exact match, ignoring case).
     *
     * @param name the doctor's name
     * @param specialty the doctor's specialty
     * @return list of matching doctors
     */
    List<Doctor> findByNameContainingIgnoreCaseAndSpecialtyIgnoreCase(
            String name,
            String specialty
    );

    /**
     * Find doctors by specialty (ignoring case).
     *
     * @param specialty the doctor's specialty
     * @return list of matching doctors
     */
    List<Doctor> findBySpecialtyIgnoreCase(String specialty);

}
package com.project.back_end.services;


import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;


import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import io.jsonwebtoken.security.Keys;


import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Component;


import javax.crypto.SecretKey;


import java.util.Date;




@Component
public class TokenService {



    private final AdminRepository adminRepository;

    private final DoctorRepository doctorRepository;

    private final PatientRepository patientRepository;



    private final String secretKey;





    /*
     * Constructor Injection
     */
    public TokenService(
            AdminRepository adminRepository,
            DoctorRepository doctorRepository,
            PatientRepository patientRepository,
            @Value("${jwt.secret}") String secretKey
    ) {


        this.adminRepository = adminRepository;

        this.doctorRepository = doctorRepository;

        this.patientRepository = patientRepository;

        this.secretKey = secretKey;

    }








    /*
     * Get JWT Signing Key
     */
    private SecretKey getSigningKey() {


        return Keys.hmacShaKeyFor(
                secretKey.getBytes()
        );

    }









    /*
     * Generate JWT Token
     *
     * Expiration:
     * 7 Days
     */
    public String generateToken(
            String email,
            String role
    ) {



        Date issuedDate =
                new Date();



        Date expirationDate =
                new Date(
                        issuedDate.getTime()
                                +
                                (1000L * 60 * 60 * 24 * 7)
                );



        return Jwts.builder()


                .setSubject(email)


                .claim(
                        "role",
                        role
                )


                .setIssuedAt(
                        issuedDate
                )


                .setExpiration(
                        expirationDate
                )


                .signWith(
                        getSigningKey(),
                        SignatureAlgorithm.HS256
                )


                .compact();

    }









    /*
     * Extract Email From Token
     */
    public String extractEmail(
            String token
    ) {


        Claims claims =
                Jwts.parser()

                        .verifyWith(
                                getSigningKey()
                        )

                        .build()

                        .parseSignedClaims(
                                token
                        )

                        .getPayload();



        return claims.getSubject();

    }









    /*
     * Get Email From Token
     *
     * Used by Service Layer
     */
    public String getEmailFromToken(
            String token
    ) {

        return extractEmail(token);

    }









    /*
     * Validate Token By Role
     *
     * ADMIN
     * DOCTOR
     * PATIENT
     */
    public boolean validateToken(
            String token,
            String role
    ) {



        try {


            String email =
                    extractEmail(token);



            if(role.equalsIgnoreCase("ADMIN")) {



                Admin admin =
                        adminRepository
                                .findByUsername(
                                        email
                                );


                return admin != null;



            } else if(role.equalsIgnoreCase("DOCTOR")) {



                Doctor doctor =
                        doctorRepository
                                .findByEmail(
                                        email
                                );


                return doctor != null;




            } else if(role.equalsIgnoreCase("PATIENT")) {



                Patient patient =
                        patientRepository
                                .findByEmail(
                                        email
                                );


                return patient != null;


            }



            return false;



        } catch(Exception e) {


            return false;

        }

    }









    /*
     * Simple Token Validation
     *
     * Check:
     * - Signature
     * - Expiration
     */
    public boolean validateToken(
            String token
    ) {


        try {


            extractEmail(token);


            return true;



        } catch(Exception e) {


            return false;

        }

    }



}
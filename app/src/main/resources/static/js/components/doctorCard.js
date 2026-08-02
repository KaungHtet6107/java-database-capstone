/*
    doctorCard.js

    Reusable Doctor Card Component

    Responsibilities:
    1. Display doctor information
    2. Show different buttons based on user role
    3. Handle admin delete action
    4. Handle patient booking action
*/


// Import required functions

import { deleteDoctor } from "../services/doctorServices.js";

import { getPatientData } from "../services/patientServices.js";

import { showBookingOverlay } from "../loggedPatient.js";



/*
    Create Doctor Card

    @param doctor
    Doctor object containing:
        id
        name
        specialization
        email
        availability
*/

export function createDoctorCard(doctor) {


    // Main card container

    const card = document.createElement("div");

    card.classList.add("doctor-card");



    // Get current logged-in role

    const role = localStorage.getItem("userRole");



    /*
        Doctor Information Section
    */

    const infoDiv = document.createElement("div");

    infoDiv.classList.add("doctor-info");



    // Doctor name

    const name = document.createElement("h3");

    name.textContent = doctor.name;



    // Doctor specialization

    const specialization = document.createElement("p");

    specialization.textContent =
        `Specialization: ${doctor.specialization}`;



    // Doctor email

    const email = document.createElement("p");

    email.textContent =
        `Email: ${doctor.email}`;



    // Doctor availability

    const availability = document.createElement("p");


    if (Array.isArray(doctor.availability)) {

        availability.textContent =
            `Available Time: ${doctor.availability.join(", ")}`;

    } else {

        availability.textContent =
            `Available Time: ${doctor.availability}`;

    }



    // Add information into info section

    infoDiv.appendChild(name);

    infoDiv.appendChild(specialization);

    infoDiv.appendChild(email);

    infoDiv.appendChild(availability);





    /*
        Button Section
    */


    const actionsDiv = document.createElement("div");

    actionsDiv.classList.add("card-actions");





    /*
        ADMIN ACTION

        Admin can delete doctor
    */

    if (role === "admin") {


        const removeBtn = document.createElement("button");

        removeBtn.textContent = "Delete";

        removeBtn.classList.add("delete-btn");



        removeBtn.addEventListener(
            "click",
            async () => {


                const confirmDelete =
                    confirm(
                        `Delete Dr. ${doctor.name}?`
                    );


                if (!confirmDelete) {

                    return;

                }



                try {


                    const token =
                        localStorage.getItem("token");



                    const response =
                        await deleteDoctor(
                            doctor.id,
                            token
                        );



                    if (response) {


                        alert(
                            "Doctor deleted successfully"
                        );


                        card.remove();


                    }



                } catch (error) {


                    console.error(
                        "Delete doctor error:",
                        error
                    );


                    alert(
                        "Failed to delete doctor"
                    );

                }


            }
        );



        actionsDiv.appendChild(removeBtn);

    }





    /*
        PATIENT WITHOUT LOGIN

        Show button but require login
    */

    else if (role === "patient") {


        const bookNow =
            document.createElement("button");


        bookNow.textContent =
            "Book Now";


        bookNow.classList.add("book-btn");



        bookNow.addEventListener(
            "click",
            () => {


                alert(
                    "Please login before booking an appointment."
                );


            }
        );



        actionsDiv.appendChild(bookNow);


    }





    /*
        LOGGED-IN PATIENT

        Allow appointment booking
    */

    else if (role === "loggedPatient") {


        const bookNow =
            document.createElement("button");


        bookNow.textContent =
            "Book Now";


        bookNow.classList.add("book-btn");



        bookNow.addEventListener(
            "click",
            async (event) => {


                try {


                    const token =
                        localStorage.getItem("token");



                    if (!token) {


                        alert(
                            "Session expired. Please login again."
                        );


                        window.location.href = "/";

                        return;

                    }



                    const patientData =
                        await getPatientData(token);



                    showBookingOverlay(
                        event,
                        doctor,
                        patientData
                    );



                } catch(error) {


                    console.error(
                        "Booking error:",
                        error
                    );


                    alert(
                        "Unable to book appointment"
                    );


                }


            }
        );



        actionsDiv.appendChild(bookNow);


    }





    /*
        Final Card Assembly
    */


    card.appendChild(infoDiv);

    card.appendChild(actionsDiv);



    return card;

}
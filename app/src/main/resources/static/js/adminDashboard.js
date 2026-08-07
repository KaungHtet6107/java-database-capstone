/*
    adminDashboard.js

    Handles the Admin Dashboard functionality.

    Responsibilities:
    - Load all doctors
    - Filter doctors
    - Open Add Doctor modal
    - Save a new doctor
*/

import { openModal } from "./components/modals.js";

import {
    getDoctors,
    filterDoctors,
    saveDoctor
} from "./services/doctorServices.js";

import {
    createDoctorCard
} from "./components/doctorCard.js";



/*
    DOM Ready
*/
document.addEventListener(
    "DOMContentLoaded",
    () => {

        /*
            Load all doctors
        */
        loadDoctorCards();



        /*
            Search bar
        */
        const searchBar =
            document.getElementById("searchBar");

        if (searchBar) {

            searchBar.addEventListener(
                "input",
                filterDoctorsOnChange
            );

        }



        /*
            Time filter
        */
        const timeFilter =
            document.getElementById("timeFilter");

        if (timeFilter) {

            timeFilter.addEventListener(
                "change",
                filterDoctorsOnChange
            );

        }



        /*
            Specialty filter
        */
        const specialtyFilter =
            document.getElementById("specialtyFilter");

        if (specialtyFilter) {

            specialtyFilter.addEventListener(
                "change",
                filterDoctorsOnChange
            );

        }



        /*
            Add Doctor button

            This button is rendered dynamically
            by header.js.
        */
        document.addEventListener(
            "click",
            (event) => {

                if (event.target.id === "addDocBtn") {

                    openModal("addDoctor");

                }

            }
        );

    }
);





/*
    Load all doctors
*/
async function loadDoctorCards() {

    const content =
        document.getElementById("content");

    if (!content) {

        return;

    }

    try {

        const response =
            await getDoctors();

        content.innerHTML = "";

        const doctors =
            response.doctors || [];

        doctors.forEach(
            doctor => {

                content.appendChild(
                    createDoctorCard(doctor)
                );

            }
        );

    }
    catch (error) {

        console.error(
            "Failed to load doctors:",
            error
        );

    }

}





/*
    Filter doctors
*/
async function filterDoctorsOnChange() {

    const content =
        document.getElementById("content");

    try {

        let name =
            document
                .getElementById("searchBar")
                .value
                .trim();

        let time =
            document
                .getElementById("timeFilter")
                .value;

        let specialty =
            document
                .getElementById("specialtyFilter")
                .value;



        name = name || null;
        time = time || null;
        specialty = specialty || null;



        const response =
            await filterDoctors(
                name,
                time,
                specialty
            );

        const doctors =
            response.doctors || [];



        if (doctors.length === 0) {

            content.innerHTML =

                `
                <h3>
                    No doctors found with the given filters.
                </h3>
                `;

            return;

        }



        renderDoctorCards(doctors);

    }
    catch (error) {

        console.error(error);

        alert(
            "Unable to filter doctors."
        );

    }

}





/*
    Render doctor cards
*/
function renderDoctorCards(doctors) {

    const content =
        document.getElementById("content");

    content.innerHTML = "";

    doctors.forEach(
        doctor => {

            content.appendChild(
                createDoctorCard(doctor)
            );

        }
    );

}





/*
    Add Doctor
*/
window.adminAddDoctor = async function () {

    const name =
        document
            .getElementById("doctorName")
            .value
            .trim();

    const email =
        document
            .getElementById("doctorEmail")
            .value
            .trim();

    const phone =
        document
            .getElementById("doctorPhone")
            .value
            .trim();

    const password =
        document
            .getElementById("doctorPassword")
            .value
            .trim();

    const specialty =
        document
            .getElementById("specialization")
            .value;

    const availability =
        Array.from(
            document.querySelectorAll(
                "input[name='availability']:checked"
            )
        ).map(
            checkbox => checkbox.value
        );



    const token =
        localStorage.getItem("token");

    if (!token) {

        alert(
            "Please login first."
        );

        return;

    }



    const doctor = {

        name,
        email,
        phone,
        password,
        specialization: specialty,
        availability

    };



    try {

        await saveDoctor(
            doctor,
            token
        );

        alert(
            "Doctor added successfully."
        );

        document
            .getElementById("modal")
            .style.display = "none";

        loadDoctorCards();

    }
    catch (error) {

        console.error(error);

        alert(
            "Failed to save doctor."
        );

    }

};
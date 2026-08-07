/*
    index.js

    Handles:
    - Opening login modals
    - Admin login
    - Doctor login
*/


/*
    Import modal functionality
*/
import { openModal } from "../components/modals.js";

/*
    Import API configuration
*/
import { API_BASE_URL } from "../config/config.js";


/*
    Login API Endpoints
*/
const ADMIN_API = `${API_BASE_URL}/admin/login`;
const DOCTOR_API = `${API_BASE_URL}/doctor/login`;


/*
    Register button events after page loads
*/
document.addEventListener(
    "DOMContentLoaded",
    () => {

        /*
            Admin Login Button
        */
        const adminButton =
            document.getElementById("adminBtn");

        if (adminButton) {

            adminButton.addEventListener(
                "click",
                () => openModal("adminLogin")
            );

        }


        /*
            Doctor Login Button
        */
        const doctorButton =
            document.getElementById("doctorBtn");

        if (doctorButton) {

            doctorButton.addEventListener(
                "click",
                () => openModal("doctorLogin")
            );

        }


        /*
            Patient Login Button
        */
        const patientButton =
            document.getElementById("patientBtn");

        if (patientButton) {

            patientButton.addEventListener(
                "click",
                () => openModal("patientLogin")
            );

        }

    }
);




/*
    Admin Login Handler
*/
window.adminLoginHandler = async function () {

    /*
        Get login values
    */
    const username =
        document.getElementById("username").value.trim();

    const password =
        document.getElementById("password").value.trim();


    /*
        Build request object
    */
    const admin = {
        username,
        password
    };


    try {

        /*
            Call Login API
        */
        const response = await fetch(
            ADMIN_API,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(admin)
            }
        );


        /*
            Invalid Login
        */
        if (!response.ok) {

            alert("Invalid username or password.");

            return;

        }


        /*
            Parse Response
        */
        const data =
            await response.json();


        /*
            Save Token
        */
        localStorage.setItem(
            "token",
            data.token
        );


        /*
            Go to Admin Dashboard
        */
        selectRole("admin");

    }
    catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server."
        );

    }

};




/*
    Doctor Login Handler
*/
window.doctorLoginHandler = async function () {

    /*
        Get login values
    */
    const email =
        document.getElementById("email").value.trim();

    const password =
        document.getElementById("password").value.trim();


    /*
        Build request object
    */
    const doctor = {
        email,
        password
    };


    try {

        /*
            Call Login API
        */
        const response = await fetch(
            DOCTOR_API,
            {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(doctor)
            }
        );


        /*
            Invalid Login
        */
        if (!response.ok) {

            alert("Invalid email or password.");

            return;

        }


        /*
            Parse Response
        */
        const data =
            await response.json();


        /*
            Save Token
        */
        localStorage.setItem(
            "token",
            data.token
        );


        /*
            Go to Doctor Dashboard
        */
        selectRole("doctor");

    }
    catch (error) {

        console.error(error);

        alert(
            "Unable to connect to the server."
        );

    }

};
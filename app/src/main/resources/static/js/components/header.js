/*
    header.js

    Dynamically renders the header depending on:
    - Current page
    - User role
    - Login token
*/


function renderHeader() {

    const headerDiv = document.getElementById("header");

    if (!headerDiv) {
        return;
    }


    /*
        Homepage check

        If user is on root page:
        - Remove previous session
        - Show only logo
    */

    if (window.location.pathname.endsWith("/")) {

        localStorage.removeItem("userRole");
        localStorage.removeItem("token");

        headerDiv.innerHTML = `

            <header class="header">

                <div class="logo-section">

                    <img 
                    src="/assets/images/logo/logo.png"
                    alt="Hospital CMS Logo"
                    class="logo-img">

                    <span class="logo-title">
                        Hospital CMS
                    </span>

                </div>

            </header>

        `;

        return;
    }



    /*
        Get user information
    */

    const role = localStorage.getItem("userRole");
    const token = localStorage.getItem("token");



    /*
        Check invalid session
    */

    if (
        (role === "loggedPatient" ||
         role === "admin" ||
         role === "doctor")
        &&
        !token
    ) {

        localStorage.removeItem("userRole");

        alert(
            "Session expired or invalid login. Please log in again."
        );

        window.location.href = "/";

        return;
    }



    /*
        Common Header
    */

    let headerContent = `

        <header class="header">

            <div class="logo-section">

                <img 
                src="/assets/images/logo/logo.png"
                alt="Hospital CMS Logo"
                class="logo-img">

                <span class="logo-title">
                    Hospital CMS
                </span>

            </div>

            <nav>

    `;



    /*
        Role based menu
    */


    if (role === "admin") {


        headerContent += `

            <button 
            id="addDocBtn"
            class="adminBtn">

                Add Doctor

            </button>


            <a href="#" onclick="logout()">
                Logout
            </a>

        `;


    } 
    else if (role === "doctor") {


        headerContent += `

            <button 
            class="adminBtn"
            onclick="window.location.href='/doctor/dashboard'">

                Home

            </button>


            <a href="#" onclick="logout()">
                Logout
            </a>

        `;


    } 
    else if (role === "patient") {


        headerContent += `

            <button 
            id="patientLogin"
            class="adminBtn">

                Login

            </button>


            <button 
            id="patientSignup"
            class="adminBtn">

                Sign Up

            </button>

        `;


    } 
    else if (role === "loggedPatient") {


        headerContent += `

            <button
            class="adminBtn"
            onclick="window.location.href='/pages/loggedPatientDashboard.html'">

                Home

            </button>


            <button
            class="adminBtn"
            onclick="window.location.href='/pages/patientAppointments.html'">

                Appointments

            </button>


            <a href="#" onclick="logoutPatient()">
                Logout
            </a>

        `;

    }



    /*
        Close header
    */

    headerContent += `

            </nav>

        </header>

    `;



    /*
        Insert into HTML

    */

    headerDiv.innerHTML = headerContent;


    attachHeaderButtonListeners();

}



/*
    Attach dynamic button events
*/

function attachHeaderButtonListeners() {


    const addDoctorButton =
        document.getElementById("addDocBtn");


    if (addDoctorButton) {

        addDoctorButton.addEventListener(
            "click",
            () => {

                openModal("addDoctor");

            }
        );

    }



    const patientLogin =
        document.getElementById("patientLogin");


    if (patientLogin) {

        patientLogin.addEventListener(
            "click",
            () => {

                openModal("patientLogin");

            }
        );

    }



    const patientSignup =
        document.getElementById("patientSignup");


    if (patientSignup) {

        patientSignup.addEventListener(
            "click",
            () => {

                openModal("patientSignup");

            }
        );

    }

}



/*
    Logout for Admin and Doctor
*/

function logout() {


    localStorage.removeItem("token");

    localStorage.removeItem("userRole");


    window.location.href = "/";

}



/*
    Logout for Patient

    Keep patient role
    because patient can login again
*/

function logoutPatient() {


    localStorage.removeItem("token");


    localStorage.setItem(
        "userRole",
        "patient"
    );


    window.location.href =
        "/pages/patientDashboard.html";

}



/*
    Run automatically
*/

document.addEventListener(
    "DOMContentLoaded",
    renderHeader
);
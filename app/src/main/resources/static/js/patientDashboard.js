// patientDashboard.js


import { 
    getDoctors,
    filterDoctors
} from "./services/doctorServices.js";


import {
    openModal
} from "./components/modals.js";


import {
    createDoctorCard
} from "./components/doctorCard.js";


import {
    patientSignup,
    patientLogin
} from "./services/patientServices.js";





document.addEventListener(
    "DOMContentLoaded",
    () => {


        loadDoctorCards();



        document
        .getElementById("patientSignup")
        ?.addEventListener(
            "click",
            () => {

                openModal(
                    "patientSignup"
                );

            }
        );



        document
        .getElementById("patientLogin")
        ?.addEventListener(
            "click",
            () => {

                openModal(
                    "patientLogin"
                );

            }
        );



        document
        .getElementById("searchBar")
        ?.addEventListener(
            "input",
            filterDoctorsOnChange
        );



        document
        .getElementById("timeFilter")
        ?.addEventListener(
            "change",
            filterDoctorsOnChange
        );



        document
        .getElementById("specialtyFilter")
        ?.addEventListener(
            "change",
            filterDoctorsOnChange
        );


    }
);







/*
    Load doctors
*/
async function loadDoctorCards(){


    try{


        const response =
            await getDoctors();



        const doctors =
            response.doctors || [];



        renderDoctors(
            doctors
        );


    }
    catch(error){


        console.error(
            "Failed loading doctors:",
            error
        );


    }

}








/*
    Render doctor cards
*/
function renderDoctors(
    doctors
){


    const content =
        document.getElementById(
            "content"
        );


    if(!content)
        return;



    content.innerHTML="";



    doctors.forEach(
        doctor=>{


            const card =
                createDoctorCard(
                    doctor
                );


            content.appendChild(
                card
            );


        }
    );


}









/*
    Filter doctors
*/
async function filterDoctorsOnChange(){


    try{


        const name =
            document
            .getElementById(
                "searchBar"
            )
            ?.value
            .trim()
            || "";



        const time =
            document
            .getElementById(
                "timeFilter"
            )
            ?.value
            || "";



        const specialty =
            document
            .getElementById(
                "specialtyFilter"
            )
            ?.value
            || "";



        const response =
            await filterDoctors(
                name,
                time,
                specialty
            );



        renderDoctors(
            response.doctors || []
        );


    }
    catch(error){


        console.error(
            error
        );


        alert(
            "Failed to filter doctors"
        );

    }


}









/*
    Patient Signup
*/
window.signupPatient =
async function(){


    try{


        const data={

            name:
            document.getElementById("name").value,


            email:
            document.getElementById("email").value,


            password:
            document.getElementById("password").value,


            phone:
            document.getElementById("phone").value,


            address:
            document.getElementById("address").value

        };



        const result =
            await patientSignup(
                data
            );



        alert(
            result.message
        );



        if(result.success){


            document
            .getElementById("modal")
            .style.display="none";


            location.reload();

        }



    }
    catch(error){


        console.error(error);


        alert(
            "Signup failed"
        );

    }

}










/*
    Patient Login
*/
window.loginPatient =
async function(){


    try{


        const data={

            email:
            document.getElementById("email").value,


            password:
            document.getElementById("password").value

        };



        const response =
            await patientLogin(
                data
            );



        if(response.ok){


            const result =
                await response.json();



            localStorage.setItem(
                "token",
                result.token
            );



            localStorage.setItem(
                "userRole",
                "loggedPatient"
            );



            window.location.href =
                "/pages/loggedPatientDashboard.html";


        }
        else{


            alert(
                "Invalid credentials"
            );


        }


    }
    catch(error){


        console.error(
            error
        );


        alert(
            "Login failed"
        );


    }

}
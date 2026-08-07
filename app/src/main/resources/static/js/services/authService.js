// authService.js

import { patientLogin } from "./patientServices.js";



window.loginPatient = async function () {


    try {


        const email =
            document
            .getElementById("email")
            .value
            .trim();



        const password =
            document
            .getElementById("password")
            .value
            .trim();




        const response =
            await patientLogin({
                email,
                password
            });





        if(response.ok){


            const result =
                await response.json();



            console.log(
                "Patient Login Response:",
                result
            );




            localStorage.setItem(
                "token",
                result.token
            );



            /*
              IMPORTANT:
              render.js uses userRole
            */
            localStorage.setItem(
                "userRole",
                "loggedPatient"
            );




            window.location.href =
                "/pages/loggedPatientDashboard.html";



        }
        else{


            const error =
                await response.json();



            alert(
                error.message ||
                "Invalid email or password"
            );


        }



    }
    catch(error){


        console.error(
            "Patient login error:",
            error
        );



        alert(
            "Login failed"
        );


    }

};
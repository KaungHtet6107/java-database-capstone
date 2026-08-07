/*
    patientServices.js

    Handles all Patient API communication
*/


import {
    API_BASE_URL
} from "../config/config.js";



const PATIENT_API =
    `${API_BASE_URL}/patient`;








/*
    Create Patient

    POST:
    /patient
*/
export async function patientSignup(data) {


    try {


        const response =
            await fetch(
                PATIENT_API,
                {

                    method:"POST",

                    headers:{
                        "Content-Type":
                            "application/json"
                    },


                    body:
                        JSON.stringify(data)

                }
            );



        const result =
            await response.json();



        return {


            success:
                response.ok,


            message:
                result.message ||
                "Signup completed"


        };



    }
    catch(error){


        console.error(
            "patientSignup error:",
            error
        );



        return {


            success:false,


            message:
                "Signup failed"


        };


    }

}









/*
    Patient Login

    POST:
    /patient/login
*/
export async function patientLogin(data){


    return fetch(

        `${PATIENT_API}/login`,

        {

            method:"POST",


            headers:{

                "Content-Type":
                    "application/json"

            },


            body:
                JSON.stringify(data)

        }

    );

}









/*
    Get Patient Data

    GET:
    /patient/{token}
*/
export async function getPatientData(token){


    try{


        const response =
            await fetch(
                `${PATIENT_API}/${token}`
            );



        const data =
            await response.json();



        if(response.ok){

            return data.patient;

        }


        return null;


    }
    catch(error){


        console.error(
            "getPatientData error:",
            error
        );


        return null;


    }

}









/*
    Get Patient Appointments

    GET:
    /patient/{id}/{user}/{token}
*/
export async function getPatientAppointments(
    id,
    user,
    token
){


    try{


        const response =
            await fetch(

                `${PATIENT_API}/${id}/${user}/${token}`

            );



        const data =
            await response.json();



        if(response.ok){

            return data.appointments;

        }



        return [];


    }
    catch(error){


        console.error(
            "getPatientAppointments error:",
            error
        );


        return [];


    }


}









/*
    Filter Appointments

    GET:
    /patient/filter/{condition}/{name}/{token}
*/
export async function filterAppointments(
    condition,
    name,
    token
){


    try{


        const response =
            await fetch(

                `${PATIENT_API}/filter/${condition}/${name}/${token}`,

                {

                    method:"GET",

                    headers:{

                        "Content-Type":
                            "application/json"

                    }

                }

            );



        if(!response.ok){


            return {

                appointments:[]

            };


        }



        return await response.json();



    }
    catch(error){


        console.error(
            "filterAppointments error:",
            error
        );



        return {


            appointments:[]

        };


    }

}
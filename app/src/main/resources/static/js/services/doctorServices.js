/*
    doctorServices.js

    Service layer for Doctor API communication.

    Responsibilities:
    - Get doctors
    - Save doctor
    - Delete doctor
    - Filter doctors
*/


import { API_BASE_URL } from "../config/config.js";



/*
    Doctor API endpoint

    Spring Controller:

    @RequestMapping("${api.path}doctor")

    Result:

    http://localhost:8080/doctor
*/
const DOCTOR_API =
    `${API_BASE_URL}/doctor`;





/*
    Function:
    getDoctors()

    Purpose:
    Get all doctors from backend
*/
export async function getDoctors() {


    try {


        const response =
            await fetch(
                DOCTOR_API,
                {
                    method: "GET"
                }
            );



        if (!response.ok) {

            console.error(
                "Failed to get doctors"
            );

            return [];

        }



        const data =
            await response.json();



        return data.doctors || [];


    }
    catch(error) {


        console.error(
            "Get doctors error:",
            error
        );


        return [];

    }

}







/*
    Function:
    deleteDoctor(id, token)


    Backend endpoint:

    DELETE:
    /doctor/delete/{id}/{token}
*/
export async function deleteDoctor(
    id,
    token
) {


    try {


        const response =
            await fetch(

                `${DOCTOR_API}/delete/${id}/${token}`,

                {
                    method: "DELETE"
                }

            );



        const data =
            await response.json();



        return {

            success:
                response.ok,

            message:
                data.message

        };


    }
    catch(error) {


        console.error(
            "Delete doctor error:",
            error
        );



        return {

            success:false,

            message:
                "Failed to delete doctor"

        };

    }

}







/*
    Function:
    saveDoctor(doctor, token)


    Backend endpoint:

    POST:
    /doctor/save/{token}
*/
export async function saveDoctor(
    doctor,
    token
) {


    try {


        const response =
            await fetch(

                `${DOCTOR_API}/save/${token}`,

                {

                    method:"POST",


                    headers:{

                        "Content-Type":
                            "application/json"

                    },


                    body:
                        JSON.stringify(doctor)

                }

            );



        const data =
            await response.json();



        return {

            success:
                response.ok,


            message:
                data.message

        };


    }
    catch(error) {


        console.error(
            "Save doctor error:",
            error
        );



        return {


            success:false,


            message:
                "Failed to save doctor"


        };

    }

}







/*
    Function:
    filterDoctors(
        name,
        time,
        speciality
    )


    Backend endpoint:

    GET:
    /doctor/filter/{name}/{time}/{speciality}

*/
export async function filterDoctors(
    name,
    time,
    speciality
) {


    try {


        /*
            Spring PathVariable cannot receive null.

            Convert null values to empty string.
        */

        name =
            name ?? "";

        time =
            time ?? "";

        speciality =
            speciality ?? "";



        const response =
            await fetch(

                `${DOCTOR_API}/filter/${name}/${time}/${speciality}`,

                {

                    method:"GET"

                }

            );



        if (!response.ok) {


            console.error(
                "Filter doctor failed"
            );


            return {

                doctors:[]

            };

        }



        const data =
            await response.json();



        return data;



    }
    catch(error) {


        console.error(
            "Filter doctor error:",
            error
        );



        alert(
            "Unable to filter doctors."
        );



        return {


            doctors:[]

        };


    }

}
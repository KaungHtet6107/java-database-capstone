// doctorDashboard.js


/*
    Import appointment service
*/
import {
    getAllAppointments
}
from "./services/appointmentRecordService.js";



/*
    Import patient row component
*/
import {
    createPatientRow
}
from "./components/patientRecordRow.js";





/*
    Global variables
*/

let tableBody;


let selectedDate =
    new Date()
        .toISOString()
        .split("T")[0];


let token =
    localStorage.getItem("token");


let patientName = null;







/*
    Page initialization
*/

document.addEventListener(
    "DOMContentLoaded",
    () => {



        /*
            Get table body after DOM loaded
        */

        tableBody =
            document.getElementById(
                "patientTableBody"
            );





        /*
            Check authentication
        */

        if(!token){

            alert(
                "Please login first."
            );

            window.location.href = "/";

            return;

        }






        /*
            Render common layout
        */

        if(
            typeof renderContent === "function"
        ){

            renderContent();

        }






        /*
            Load today's appointments
        */

        loadAppointments();








        /*
            Search patient
        */

        const searchBar =
            document.getElementById(
                "searchBar"
            );



        if(searchBar){


            searchBar.addEventListener(
                "input",
                () => {


                    const value =
                        searchBar.value.trim();



                    patientName =
                        value === ""
                        ? null
                        : value;



                    loadAppointments();


                }
            );


        }








        /*
            Today button
        */

        const todayBtn =
            document.getElementById(
                "todayBtn"
            );



        if(todayBtn){


            todayBtn.addEventListener(
                "click",
                () => {



                    selectedDate =
                        new Date()
                        .toISOString()
                        .split("T")[0];




                    const datePicker =
                        document.getElementById(
                            "datePicker"
                        );



                    if(datePicker){

                        datePicker.value =
                            selectedDate;

                    }




                    loadAppointments();



                }
            );


        }









        /*
            Date picker
        */

        const datePicker =
            document.getElementById(
                "datePicker"
            );



        if(datePicker){


            datePicker.value =
                selectedDate;



            datePicker.addEventListener(
                "change",
                () => {


                    selectedDate =
                        datePicker.value;



                    loadAppointments();


                }
            );

        }



    }
);









/*
    Function:
    loadAppointments

    Purpose:
    Fetch and render appointments
*/

async function loadAppointments(){


    try{


        const appointments =
            await getAllAppointments(
                selectedDate,
                patientName,
                token
            );



        tableBody.innerHTML = "";






        /*
            No appointment found
        */

        if(
            !appointments ||
            appointments.length === 0
        ){


            tableBody.innerHTML =
            `
                <tr>

                    <td colspan="5">
                        No Appointments found for today.
                    </td>

                </tr>
            `;


            return;

        }









        /*
            Create patient rows
        */

        appointments.forEach(
            appointment => {



                const patient = {


                    id:
                    appointment.patient?.id,



                    name:
                    appointment.patient?.name,



                    phone:
                    appointment.patient?.phone,



                    email:
                    appointment.patient?.email


                };






                const row =
                    createPatientRow(
                        patient,
                        appointment
                    );





                tableBody.appendChild(row);



            }
        );



    }
    catch(error){


        console.error(
            "Error loading appointments:",
            error
        );



        tableBody.innerHTML =
        `
            <tr>

                <td colspan="5">

                    Error loading appointments.
                    Try again later.

                </td>

            </tr>
        `;


    }


}
/*
    footer.js

    Dynamically renders the footer section of Hospital CMS.

    Responsibilities:
    1. Find the footer placeholder element (#footer)
    2. Inject footer HTML content
    3. Display hospital logo, copyright, and useful links
*/


function renderFooter() {

    // Select footer container from HTML
    const footer = document.getElementById("footer");


    // Stop execution if footer element does not exist
    if (!footer) {
        console.error("Footer element not found");
        return;
    }


    // Inject footer HTML content
    footer.innerHTML = `

        <footer class="footer">

            <div class="footer-container">


                <!-- Logo and Copyright Section -->
                <div class="footer-logo">

                    <img 
                        src="/assets/images/logo/logo.png"
                        alt="Hospital CMS Logo"
                    >

                    <p>
                        © Copyright 2026. All Rights Reserved by Hospital CMS.
                    </p>

                </div>



                <!-- Footer Links Section -->
                <div class="footer-links">


                    <!-- Company Links -->
                    <div class="footer-column">

                        <h4>Company</h4>

                        <a href="#">
                            About
                        </a>

                        <a href="#">
                            Careers
                        </a>

                        <a href="#">
                            Press
                        </a>

                    </div>




                    <!-- Support Links -->
                    <div class="footer-column">

                        <h4>Support</h4>

                        <a href="#">
                            Account
                        </a>

                        <a href="#">
                            Help Center
                        </a>

                        <a href="#">
                            Contact Us
                        </a>

                    </div>




                    <!-- Legal Links -->
                    <div class="footer-column">

                        <h4>Legals</h4>

                        <a href="#">
                            Terms & Conditions
                        </a>

                        <a href="#">
                            Privacy Policy
                        </a>

                        <a href="#">
                            Licensing
                        </a>

                    </div>


                </div>


            </div>


        </footer>

    `;

}



// Automatically render footer after page loading

document.addEventListener(
    "DOMContentLoaded",
    renderFooter
);
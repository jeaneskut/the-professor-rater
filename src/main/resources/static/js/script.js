
class StaffRating {
    constructor(id, name, email, role, clarity, niceness, knowledge) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.clarity = clarity;
        this.niceness = niceness;
        this.knowledge = knowledge;
    }

    // Calculates the average of the three scores
    getOverallScore() {
        return (this.clarity + this.niceness + this.knowledge) / 3;
    }
}

// Temporary example data
const staff = [
    new StaffRating(1, "John Doe", "jdoe@sfu.ca",
        "PROF", 9, 8, 10),

    new StaffRating(2, "Jane Doe", "janedoe@sfu.ca",
        "TA", 8, 10, 8),

    new StaffRating(3, "Napoleon Bonaparte", "nbonaparte@sfu.ca",
        "INSTRUCTOR", 7, 8, 9)
];


const staffList = document.getElementById("staff-list");

if (staffList) {
    const searchInput = document.getElementById("search");
    const roleSelect = document.getElementById("role");
    const sortSelect = document.getElementById("sort");
    const results = document.getElementById("results");
    const message = document.getElementById("message");

    // Displays the staff cards
    function showStaff() {
        const search = searchInput.value.trim().toLowerCase();
        const selectedRole = roleSelect.value;
        const sort = sortSelect.value;

        // Find staff matching the search and role
        let filteredStaff = Array.from(staffList.children).filter(card => {
            const name = card.querySelector("h3").textContent.trim();
            const role = card.querySelector(".role").textContent.trim();

            const matchesName = name.toLowerCase().includes(search);
            const matchesRole = selectedRole === "ALL"
                || role === selectedRole;

            return matchesName && matchesRole;
        });

        // Sort the results
        if (sort === "name") {
            filteredStaff.sort((a, b) => {
                const nameA = a.querySelector("h3").textContent.trim();
                const nameB = b.querySelector("h3").textContent.trim();

                return nameA.localeCompare(nameB);
            });
        } else if (sort === "high") {
            filteredStaff.sort((a, b) => {
                const scoreA = parseFloat(
                    a.querySelector("strong").textContent
                );
                const scoreB = parseFloat(
                    b.querySelector("strong").textContent
                );

                return scoreB - scoreA;
            });
        } else if (sort === "low") {
            filteredStaff.sort((a, b) => {
                const scoreA = parseFloat(
                    a.querySelector("strong").textContent
                );
                const scoreB = parseFloat(
                    b.querySelector("strong").textContent
                );

                return scoreA - scoreB;
            });
        }

        // Hide cards that don't match the search or role
        Array.from(staffList.children).forEach(card => {
            card.hidden = !filteredStaff.includes(card);
        });

        // Move matching cards into the selected sort order
        filteredStaff.forEach(card => {
            staffList.appendChild(card);
        });

        // Display a message if no results match
        message.hidden = filteredStaff.length !== 0
            || staffList.children.length === 0;

        results.textContent = "Showing " + filteredStaff.length
            + " staff member(s).";
    }

    // Update the page when the user changes a search or filter
    searchInput.addEventListener("input", showStaff);
    roleSelect.addEventListener("change", showStaff);
    sortSelect.addEventListener("change", showStaff);

    // Display the initial staff list
    showStaff();
}



// Detail page: delete button
const deleteButton = document.getElementById("delete-button");

if (deleteButton) {
    const deleteMessage = document.getElementById("delete-message");

    deleteButton.addEventListener("click", function () {
        const confirmed = confirm(
            "Are you sure you want to delete this rating?"
        );

        if (confirmed) {
            deleteMessage.textContent =
                "Demo only: this rating has not been deleted from a database.";

            deleteMessage.hidden = false;
        }
    });
}


// Create and edit pages: form validation
const form = document.getElementById("rating-form");

if (form) {
    const formMessage = document.getElementById("form-message");

    form.addEventListener("submit", function (event) {

        document.querySelectorAll(".field-error").forEach(function (error) {
            error.textContent = "";
            error.hidden = true;
        });

        formMessage.hidden = true;

        let valid = true;

        function showError(id, text) {
            const error = document.getElementById(id + "-error");

            if (error) {
                error.textContent = text;
                error.hidden = false;
            }
            
            valid = false;
        }

        const name = document.getElementById("name").value.trim();

        if (name === "") {
            showError("name", "Name is required.");
        }

        const emailInput = document.getElementById("email");
        const email = emailInput.value.trim();

        if (email === "") {
            showError("email", "Email is required.");
        } else if (!emailInput.validity.valid) {
            showError("email", "Enter a valid email address.");
        }

        if (document.getElementById("roleType").value === "") {
            showError("role", "Please select a role.");
        }

        const scores = [
            ["clarity", "Clarity"],
            ["niceness", "Niceness"],
            ["knowledge", "Knowledge"]
        ];

        scores.forEach(function ([id, label]) {
            const input = document.getElementById(id);

            if (input.value === "") {
                showError(id, label + " score is required.");
            } else if (!input.validity.valid) {
                showError(
                    id,
                    label + " must be a whole number from 1 to 10."
                );
            }
        });

        const comment = document.getElementById("comment").value;

        if (comment.length > 1000) {
            showError("comment", "Comment cannot exceed 1000 characters.");
        }

        if (!valid) {
            event.preventDefault();
            formMessage.textContent = "Please correct the errors above.";
            formMessage.hidden = false;
        }

        formMessage.textContent =
            "The form is valid! Saving to the database will be implemented next.";

        formMessage.hidden = false;
    });
}
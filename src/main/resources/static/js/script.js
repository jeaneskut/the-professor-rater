
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
const searchInput = document.getElementById("search");
const roleSelect = document.getElementById("role");
const sortSelect = document.getElementById("sort");
const results = document.getElementById("results");
const message = document.getElementById("message");

// Converts role codes into readable titles
function getRoleName(role) {
    if (role === "PROF") return "Professor";
    if (role === "TA") return "Teaching Assistant";
    if (role === "INSTRUCTOR") return "Instructor";
    return "Staff";
}

// Displays the staff cards
function showStaff() {
    const search = searchInput.value.trim().toLowerCase();
    const selectedRole = roleSelect.value;
    const sort = sortSelect.value;

    // Find staff matching the search and role
    let filteredStaff = staff.filter(person => {
        const matchesName = person.name.toLowerCase().includes(search);
        const matchesRole = selectedRole === "ALL"
            || person.role === selectedRole;

        return matchesName && matchesRole;
    });

    // Sort the results
    if (sort === "name") {
        filteredStaff.sort((a, b) => a.name.localeCompare(b.name));
    } else if (sort === "high") {
        filteredStaff.sort((a, b) =>
            b.getOverallScore() - a.getOverallScore());
    } else if (sort === "low") {
        filteredStaff.sort((a, b) =>
            a.getOverallScore() - b.getOverallScore());
    }

    // Clear the previous results
    staffList.innerHTML = "";

    // Display a message if no results match
    message.hidden = filteredStaff.length !== 0;

    results.textContent = "Showing " + filteredStaff.length
        + " staff member(s).";

    // Create a card for each staff member
    filteredStaff.forEach(person => {
        const card = document.createElement("div");
        card.className = "staff-card";

        const heading = document.createElement("h3");
        heading.textContent = person.name;

        const role = document.createElement("p");
        role.className = "role";
        role.textContent = getRoleName(person.role);

        const email = document.createElement("p");
        email.textContent = person.email;

        const score = document.createElement("p");
        score.className = "score";
        score.textContent = person.getOverallScore().toFixed(1) + "/10";

        const breakdown = document.createElement("p");
        breakdown.textContent = "Clarity: " + person.clarity
            + "/10 | Niceness: " + person.niceness
            + "/10 | Knowledge: " + person.knowledge + "/10";

        const link = document.createElement("a");
        link.className = "details-button";
        link.href = "/ratings/" + person.id;
        link.textContent = "View Details";

        card.appendChild(heading);
        card.appendChild(role);
        card.appendChild(email);
        card.appendChild(score);
        card.appendChild(breakdown);
        card.appendChild(link);

        staffList.appendChild(card);
    });
}

// Update the page when the user changes a search or filter
searchInput.addEventListener("input", showStaff);
roleSelect.addEventListener("change", showStaff);
sortSelect.addEventListener("change", showStaff);

// Display the initial staff list
showStaff();
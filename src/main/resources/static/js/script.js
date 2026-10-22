// Grabs the staff list
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

        // Finds staff matching the search and role
        let filteredStaff = Array.from(staffList.children).filter(card => {
            const name = card.querySelector("h3").textContent.trim();
            const role = card.querySelector(".role").textContent.trim();

            const matchesName = name.toLowerCase().includes(search);
            const matchesRole = selectedRole === "ALL"
                || role === selectedRole;

            return matchesName && matchesRole;
        });

        // Sorts the results
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

        // Hides cards that don't match the search or role
        Array.from(staffList.children).forEach(card => {
            card.hidden = !filteredStaff.includes(card);
        });

        // Moves matching cards into the selected sort order
        filteredStaff.forEach(card => {
            staffList.appendChild(card);
        });

        // Displays a message if no results match
        message.hidden = filteredStaff.length !== 0
            || staffList.children.length === 0;

        results.textContent = "Showing " + filteredStaff.length
            + " staff member(s).";
    }

    // Updates the page when the user changes a search or filter
    searchInput.addEventListener("input", showStaff);
    roleSelect.addEventListener("change", showStaff);
    sortSelect.addEventListener("change", showStaff);

    // Displays the initial staff list
    showStaff();
}
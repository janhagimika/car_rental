document.addEventListener("DOMContentLoaded", () => {
    const customerId = new URLSearchParams(window.location.search).get("id");

    // Fetch customer details and rental history
    fetch(`http://localhost:8080/api/customers/${customerId}`)
        .then(response => response.json())
        .then(customer => {
            displayCustomerInfo(customer);
            displayRentalHistory(customer.rentalHistory);
        })
        .catch(error => console.error("Error fetching customer details:", error));

    // Populate cars dropdown
    fetch("http://localhost:8080/api/cars/available")
        .then(response => response.json())
        .then(cars => {
            populateCarDropdown(cars);
        })
        .catch(error => console.error("Error fetching available cars:", error));


    // Add rental form submission
    document.getElementById("add-rental-form").addEventListener("submit", event => {
        event.preventDefault();
        const formData = new FormData(event.target);
        const rentalData = {
            car: { id: formData.get("car") },
            customer: { id: customerId },
            rentalDate: formData.get("rentalDate"),
            returnDate: formData.get("returnDate")
        };

        fetch("http://localhost:8080/api/rentals", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(rentalData),
        })
            .then(response => response.json())
            .then(() => {
                /*alert("Rental added successfully");
                location.reload();*/
            })
            .catch(error => console.error("Error adding rental:", error));
    });
});

function displayCustomerInfo(customer) {
    document.getElementById("customer-name").textContent = `${customer.firstname} ${customer.surname}`;
    document.getElementById("customer-email").textContent = `Email: ${customer.emailAddress}`;
    document.getElementById("customer-phone").textContent = `Phone: ${customer.phoneNumber}`;
    document.getElementById("customer-address").textContent = `Address: ${customer.address || "N/A"}`;
}

function displayRentalHistory(rentals) {
    const rentalHistoryTableBody = document.querySelector("#rental-history tbody");
    rentalHistoryTableBody.innerHTML = "";
    rentals.forEach(rental => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${rental.car.model}</td>
            <td>${rental.rentalDate}</td>
            <td>${rental.returnDate || "N/A"}</td>
            <td>${rental.status}</td>
            <td>
                ${rental.status === "PENDING" ? `<button onclick="completeRental(${rental.id})">Mark as Returned</button>` : ""}
            </td>
        `;
        rentalHistoryTableBody.appendChild(row);
    });
}

function populateCarDropdown(cars) {
    const carSelect = document.getElementById("car");
    carSelect.innerHTML = cars
        .map(car => `<option value="${car.id}">${car.brand} ${car.model}</option>`)
        .join("");
}


function completeRental(rentalId) {
    const conditionOnReturn = prompt("Enter the condition of the car upon return:");
    fetch(`http://localhost:8080/api/rentals/${rentalId}/return`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ conditionOnReturn: conditionOnReturn }),
    })
        .then(() => {
            alert("Rental marked as returned");
            location.reload();
        })
        .catch(error => console.error("Error completing rental:", error));
}

function navigateTo(page) {
    window.location.href = page;
}

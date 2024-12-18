let currentRentalId = null;

function openModal(rentalId) {
    currentRentalId = rentalId; // Store the rental ID
    document.getElementById("returnModal").style.display = "block";
}

function closeModal() {
    currentRentalId = null;
    document.getElementById("returnModal").style.display = "none";
}

document.addEventListener("DOMContentLoaded", () => {
    const customerId = new URLSearchParams(window.location.search).get("id");

    // Fetch customer details and rental history
    fetch(`http://localhost:8080/api/customers/${customerId}`)
        .then(handleResponse) // Use centralized error handler
        .then(customer => {
            displayCustomerInfo(customer);
            displayRentalHistory(customer.rentalHistory);
        })
        .catch(error => console.error("Error fetching customer details:", error));

    // Populate cars dropdown
    fetch("http://localhost:8080/api/cars/available")
        .then(handleResponse) // Use centralized error handler
        .then(cars => {
            populateCarDropdown(cars);
        })
        .catch(error => console.error("Error fetching available cars:", error));

    // Add rental form submission
    document.getElementById("add-rental-form").addEventListener("submit", event => {
        event.preventDefault();
        const formData = new FormData(event.target);
        const rentalDate = new Date(formData.get("rentalDate")).toISOString();
        const plannedReturnDate = new Date(formData.get("plannedReturnDate")).toISOString();

        const rentalData = {
            car: { id: formData.get("car") },
            customer: { id: customerId },
            rentalDate: rentalDate, // ISO formatted rentalDate
            plannedReturnDate: plannedReturnDate, // ISO formatted plannedReturnDate
        };

        fetch("http://localhost:8080/api/rentals", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(rentalData),
        })
            .then(handleResponse) // Use centralized error handler
            .then(() => {
                alert("Rental added successfully");
                location.reload();
            })
            .catch(error => console.error("Error adding rental:", error));
    });
});

function handleResponse(response) {
    if (response.ok) {
        return response.json();
    } else {
        return response.json().then(errorData => {
            const errorMessage = Object.values(errorData).join("\n") || `Error: ${response.status}`;
            console.error("Error Details:", errorData);
            alert(errorMessage);
            throw new Error(errorMessage);
        });
    }
}

function displayCustomerInfo(customer) {
    document.getElementById("customer-name").textContent = `${customer.firstname} ${customer.surname}`;
    document.getElementById("customer-email").textContent = `Email: ${customer.emailAddress}`;
    document.getElementById("customer-phone").textContent = `Phone: ${customer.phoneNumber}`;
    document.getElementById("customer-address").textContent = `Address: ${customer.address || "N/A"}`;
}

function displayRentalHistory(rentals) {
    const rentalHistoryTableBody = document.querySelector("#rental-history tbody");
    rentalHistoryTableBody.innerHTML = ""; // Clear table content first
    rentals.forEach(rental => {
        const row = document.createElement("tr");
        row.innerHTML = `
            <td>${rental.car.brand} ${rental.car.model}</td>
            <td>${rental.rentalDate}</td>
            <td>${rental.plannedReturnDate || "N/A"}</td>
            <td>${rental.returnDate || "N/A"}</td>
            <td>${rental.conditionOnReturn || "N/A"}</td>
            <td>${rental.status}</td>
            <td>
                ${rental.status === "PENDING"
            ? `<button onclick="openModal(${rental.id})">Return Car</button>`
            : "Completed"}
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

// Handle modal form submission
document.getElementById("return-form").addEventListener("submit", event => {
    event.preventDefault();
    const conditionOnReturn = document.getElementById("return-condition").value;

    fetch(`http://localhost:8080/api/rentals/${currentRentalId}/return`, {
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(conditionOnReturn),
    })
        .then(handleResponse) // Use centralized error handler
        .then(() => {
            alert("Rental marked as returned");
            location.reload();
        })
        .catch(error => console.error("Error completing rental:", error));

    closeModal(); // Close the modal
});

function navigateTo(page) {
    window.location.href = page;
}

// Function to display the list of customers
function displayCustomers(customers) {
    const customerList = document.getElementById("customer-list");
    customerList.innerHTML = ""; // Clear the list first

    customers.forEach((customer) => {
        // Create a container for the customer
        const customerDiv = document.createElement("div");
        customerDiv.classList.add("customer-item");

        // Add customer details
        customerDiv.innerHTML = `
            <span>${customer.firstname} ${customer.surname} - ${customer.emailAddress} (${customer.phoneNumber})</span>
        `;

        // Create a button for viewing rental history
        const viewRentalsButton = document.createElement("button");
        viewRentalsButton.textContent = "View Rentals";
        viewRentalsButton.classList.add("view-rentals-button");
        viewRentalsButton.onclick = () => {
            window.location.href = `customer-details.html?id=${customer.id}`;
        };

        // Append the button to the customer container
        customerDiv.appendChild(viewRentalsButton);

        // Add the customer container to the list
        customerList.appendChild(customerDiv);
    });
}

// Fetch and display customers
fetch("http://localhost:8080/api/customers")
    .then((response) => response.json())
    .then((customers) => displayCustomers(customers))
    .catch((error) => console.error("Error fetching customers:", error));

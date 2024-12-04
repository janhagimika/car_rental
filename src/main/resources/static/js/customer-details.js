document.addEventListener("DOMContentLoaded", () => {
    const urlParams = new URLSearchParams(window.location.search);
    const customerId = urlParams.get("id");

    fetch(`api/customers/${customerId}`)
        .then((response) => response.json())
        .then((customer) => {
            const rentalHistoryList = document.getElementById("rental-history");
            const rentals = customer.rentalHistory;
            if (rentals.length === 0) {
                rentalHistoryList.innerHTML = "<li>No rentals found</li>";
            } else {
                rentals.forEach((rental) => {
                    const li = document.createElement("li");
                    li.textContent = `Car: ${rental.car.brand} ${rental.car.model}, Rental Date: ${rental.rentalDate}, Return Date: ${rental.returnDate}`;
                    rentalHistoryList.appendChild(li);
                });
            }
        })
        .catch((error) => console.error("Error fetching rental history:", error));
});

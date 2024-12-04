document.addEventListener("DOMContentLoaded", () => {
    fetch("api/customers")
        .then((response) => response.json())
        .then((customers) => {
            const customerList = document.getElementById("customer-list");
            customers.forEach((customer) => {
                const li = document.createElement("li");
                li.textContent = `${customer.firstname} ${customer.surname} - ${customer.emailAddress} (${customer.phoneNumber})`;
                li.addEventListener("click", () => {
                    window.location.href = `customer-details.html?id=${customer.id}`;
                });
                customerList.appendChild(li);
            });
        })
        .catch((error) => console.error("Error fetching customers:", error));
});

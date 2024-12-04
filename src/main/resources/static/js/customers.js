const customerApiUrl = 'http://localhost:8080/api/customers';
const carApiUrl = 'http://localhost:8080/api/cars';
const rentalApiUrl = 'http://localhost:8080/api/rentals';

// Fetch all customers
function fetchCustomers() {
    fetch(customerApiUrl)
        .then((response) => response.json())
        .then((customers) => {
            const customerList = document.getElementById('customer-list');
            const customerSelect = document.getElementById('customer-id');
            customerList.innerHTML = '';
            customerSelect.innerHTML = '<option value="">Select a customer</option>';

            customers.forEach((customer) => {
                const li = document.createElement('li');
                li.textContent = `${customer.name} - ${customer.contactDetails}`;
                customerList.appendChild(li);

                const option = document.createElement('option');
                option.value = customer.id;
                option.textContent = customer.name;
                customerSelect.appendChild(option);
            });
        })
        .catch((error) => console.error('Error fetching customers:', error));
}

// Fetch all cars
function fetchCars() {
    fetch(carApiUrl)
        .then((response) => response.json())
        .then((cars) => {
            const carSelect = document.getElementById('car-id');
            carSelect.innerHTML = '<option value="">Select a car</option>';

            cars.forEach((car) => {
                const option = document.createElement('option');
                option.value = car.id;
                option.textContent = `${car.brand} ${car.model} (${car.yearOfManufacture})`;
                carSelect.appendChild(option);
            });
        })
        .catch((error) => console.error('Error fetching cars:', error));
}

// Add a new customer
document.getElementById('add-customer-form').addEventListener('submit', (event) => {
    event.preventDefault();

    const customer = {
        name: document.getElementById('customer-name').value,
        contactDetails: document.getElementById('customer-contact').value,
    };

    fetch(customerApiUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(customer),
    })
        .then((response) => response.json())
        .then(() => {
            fetchCustomers(); // Refresh customer list
            document.getElementById('add-customer-form').reset();
        })
        .catch((error) => console.error('Error adding customer:', error));
});

// Create a new rental
document.getElementById('create-rental-form').addEventListener('submit', (event) => {
    event.preventDefault();

    const rental = {
        customer: { id: document.getElementById('customer-id').value },
        car: { id: document.getElementById('car-id').value },
        rentalDate: new Date(document.getElementById('rental-date').value).toISOString(),
    };

    fetch(rentalApiUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(rental),
    })
        .then((response) => response.json())
        .then(() => {
            alert('Rental created successfully!');
            document.getElementById('create-rental-form').reset();
        })
        .catch((error) => console.error('Error creating rental:', error));
});

// Initial fetches
fetchCustomers();
fetchCars();

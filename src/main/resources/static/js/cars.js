const apiBaseUrl = 'http://localhost:8080/api/cars';
let editMode = false;
let editCarId = null; // To store the car ID being edited

// Fetch all cars
function fetchCars() {
    fetch(apiBaseUrl)
        .then(handleResponse)
        .then((cars) => {
            const carList = document.getElementById('car-list');
            carList.innerHTML = ''; // Clear the list
            cars.forEach((car) => {
                const li = document.createElement('li');
                li.innerHTML = `
                    ${car.brand} ${car.model} (${car.yearOfManufacture}) - ${car.color}, ${car.mileage} km
                    <button onclick="populateFormForEdit(${car.id}, '${car.brand}', '${car.model}', ${car.yearOfManufacture}, '${car.color}', ${car.mileage})">Edit</button>
                    <button onclick="deleteCar(${car.id})">Delete</button>
                `;
                carList.appendChild(li);
            });
        })
        .catch((error) => console.error('Error fetching cars:', error));
}

// Add or update car
document.getElementById('add-car-form').addEventListener('submit', (event) => {
    event.preventDefault();

    const car = {
        brand: document.getElementById('brand').value,
        model: document.getElementById('model').value,
        yearOfManufacture: parseInt(document.getElementById('year').value, 10),
        color: document.getElementById('color').value,
        mileage: parseInt(document.getElementById('mileage').value, 10),
    };

    const fetchOptions = {
        method: editMode ? 'PUT' : 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car),
    };

    const fetchUrl = editMode ? `${apiBaseUrl}/${editCarId}` : apiBaseUrl;

    fetch(fetchUrl, fetchOptions)
        .then(handleResponse)
        .then(() => {
            alert(editMode ? 'Car updated successfully!' : 'Car added successfully!');
            fetchCars(); // Refresh the list
            document.getElementById('add-car-form').reset(); // Clear form
            resetForm(); // Reset edit mode
        })
        .catch((error) => console.error('Error saving car:', error));
});

// Populate the form for editing
function populateFormForEdit(id, brand, model, year, color, mileage) {
    editMode = true;
    editCarId = id;

    document.getElementById('brand').value = brand;
    document.getElementById('model').value = model;
    document.getElementById('year').value = year;
    document.getElementById('color').value = color;
    document.getElementById('mileage').value = mileage;

    document.getElementById('submit-button').textContent = 'Update Car'; // Update button text
}

// Delete a car
// Delete a car
function deleteCar(carId) {
    if (!confirm('Are you sure you want to delete this car?')) return;

    fetch(`${apiBaseUrl}/${carId}`, {
        method: 'DELETE',
    })
        .then(handleResponse)
        .then((message) => {
            alert(message || 'Car deleted successfully!');
            fetchCars(); // Refresh the list
        })
        .catch((error) => console.error('Error deleting car:', error));
}




// Reset form and edit mode
function resetForm() {
    editMode = false;
    editCarId = null;
    document.getElementById('submit-button').textContent = 'Add Car'; // Reset button text
}

// Initial fetch
fetchCars();

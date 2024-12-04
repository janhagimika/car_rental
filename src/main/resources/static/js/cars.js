const apiBaseUrl = 'http://localhost:8080/api/cars';

// Fetch all cars
function fetchCars() {
    fetch(apiBaseUrl)
        .then((response) => response.json())
        .then((cars) => {
            const carList = document.getElementById('car-list');
            carList.innerHTML = ''; // Clear the list
            cars.forEach((car) => {
                const li = document.createElement('li');
                li.textContent = `${car.brand} ${car.model} (${car.yearOfManufacture}) - ${car.color}, ${car.mileage} km`;
                carList.appendChild(li);
            });
        })
        .catch((error) => console.error('Error fetching cars:', error));
}

// Add a new car
document.getElementById('add-car-form').addEventListener('submit', (event) => {
    event.preventDefault();

    const car = {
        brand: document.getElementById('brand').value,
        model: document.getElementById('model').value,
        yearOfManufacture: parseInt(document.getElementById('year').value, 10),
        color: document.getElementById('color').value,
        mileage: parseInt(document.getElementById('mileage').value, 10),
    };

    fetch(apiBaseUrl, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(car),
    })
        .then((response) => response.json())
        .then(() => {
            fetchCars(); // Refresh the list
            document.getElementById('add-car-form').reset(); // Clear form
        })
        .catch((error) => console.error('Error adding car:', error));
});

// Initial fetch
fetchCars();

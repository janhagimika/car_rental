window.handleResponse = function(response) {
    if (response.ok) {
        const contentType = response.headers.get("Content-Type");
        if (contentType && contentType.includes("application/json")) {
            return response.json(); // Parse JSON response
        } else {
            return response.text(); // Return plain text
        }
    } else {
        return response.json().then((errorData) => {
            const errorMessage = Object.values(errorData).join('\n') || `Error: ${response.status}`;
            console.error('Error Details:', errorData);
            alert(errorMessage);
            throw new Error(errorMessage);
        });
    }
};

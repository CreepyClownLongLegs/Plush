const express = require('express');
const app = express();
const fs = require('fs');
const path = require('path');

// Load routes dynamically
const routesDir = path.join(__dirname, 'routes');
fs.readdirSync(routesDir).forEach(file => {
    const route = require(path.join(routesDir, file));
    app.use(route.path, route.router);
});

// DEV ONLY
if (process.env.NODE_ENV === 'development') {
    const airDropService = require('./services/airDropService');
    airDropService.airdropSOL();
}

const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
    console.log(`Server is running on port ${PORT}`);
});


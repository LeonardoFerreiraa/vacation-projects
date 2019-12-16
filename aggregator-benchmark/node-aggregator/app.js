const express = require('express');

const aggregator = require('./routes/aggregator');

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use('/aggregations', aggregator);

module.exports = app;

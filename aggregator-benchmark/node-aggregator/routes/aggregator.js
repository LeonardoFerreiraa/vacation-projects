const express = require('express');
const router = express.Router();

const axios = require('axios');

const httpBinClient = axios.create({
    baseURL: 'https://httpbin.org/'
});

const jsonPlaceHolderClient = axios.create({
    baseURL: 'https://jsonplaceholder.typicode.com/'
});

router.get('/', function (req, res, next) {
    let todoPromise = httpBinClient.post('/anything', { number: 200 })
        .then(({ data }) => {
            let number = data.json.number;

            return jsonPlaceHolderClient.get(`/todos/${number}`)
                .then(({ data }) => data)
        });

    let uuidPromise = httpBinClient.get('/uuid')
        .then(({ data }) => data);

    Promise.all([todoPromise, uuidPromise])
        .then(results => {
            let todo = results[0];
            let uuid = results[1];

            let aggregatedResult = {
                todoTitle: todo.title,
                uuid: uuid.uuid
            };

            res.json(aggregatedResult)
        })
});

module.exports = router;

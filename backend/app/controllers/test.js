var express = require('express');
var router = express.Router();
var User = require('../models/user');


router.get('/', function(req, res) {
    res.json({ message: 'Welcome to the coolest API on earth!' });
});
module.exports = router;


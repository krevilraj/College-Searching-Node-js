var exp = require('express');
var appRouter = exp.Router();
var UserModel = require('../models/UserModel');
var CollegeModel = require('../models/CollegeModel');
var jsonToken = require('jsonwebtoken');
var bcrypt = require('bcrypt');
var app = exp();

// Authorize User Check
appRouter.use(function (req, res, next) {

    // check header or url parameters or post parameters for token
    var token = req.body.token || req.param('token') || req.headers['x-access-token'];
    if (token) {
        jsonToken.verify(token, "cmessage", function (err, decoded) {
            if (err) {
                return res.json({response: false, message: 'Failed to authenticate token.'});
            }
        });
    } else {
        return res.status(403).send({
            response: false,
            message: 'No token provided.'
        });
    }
    // Check Authorize User
    UserModel.findOne({_id: req.body.id}, function (err, user) {
        if (err) {res.json({"response": false,"message": 'Something is wrong'});}
        else if (!user) {res.json({"response": false,"message": 'You are not Allowed'});}
        else if (user) {
            if (user.admin_status) {
                next();
            } else {
                res.json({"response": false,"message": 'You are not Authorize'});
            }
        }
    });

});

//insert API
appRouter.post('/addCollege', (req, res) => {
    var newCollege = new CollegeModel({
        college_name: req.body.college_name,
        location: req.body.location,
        website: req.body.website,
        phone: req.body.phone,
        course: req.body.course,
        description: req.body.content
    });

    newCollege.save(function (err, data) {
        if (err) {
            res.json({"response": false,'message': err});
        } else {
            res.json({"response": true,'message': 'Success!!! Added to College'});
        }
    });
});


//listing API
appRouter.post('/listCollege', function (req, res, next) {
    CollegeModel.find((err, docs) => {
        if (!err) {
            res.render("collegeList", {
                datas: docs,
                layout: ""
            });
        }
        else {
            res.json({"response": false,'message': 'Request Failed'});
        }
    }).sort({'_id': -1});
});

//delete API
appRouter.post('/deleteCollege', (req, res) => {
    CollegeModel.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({"response": true,'message': 'College Deleted Successfully!!'});
        }
        else {
            res.json({"response": false,'message': 'Error in delete :' + err});
        }
    });
});

//find Detail API
appRouter.post('/editCollege', (req, res) => {
    CollegeModel.findById(req.body._id, (err, data) => {
        if (!err) {
            res.json(data);
        }
    });
});

//Update API
appRouter.post('/updateCollege', (req, res) => {
    var request_data = req.body;
    request_data.description = req.body.content;
    CollegeModel.findOneAndUpdate({_id: req.body._id}, request_data, {new: true}, (err, doc) => {
        if (!err) {
            res.json({"response": true,'message': 'Success!!! College is updated'});
        }
        else {
            res.json({"response": false,'message': 'Error in Update : ' + err});
        }
    });
});

module.exports = appRouter;


var exp = require('express');
var appRouter = exp.Router();
var UserModel = require('../models/UserModel');
var FeedbackModel = require('../models/ContactModel');
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
                return res.json({success: false, message: 'Failed to authenticate token.'});
            }
        });
    } else {
        return res.status(403).send({
            success: false,
            message: 'No token provided.'
        });
    }
    // Check Authorize User
    UserModel.findOne({_id: req.body.id}, function (err, user) {
        if (err) {res.json({"response": 'Something is wrong'});}
        else if (!user) {res.json({"response": 'You are not Allowed'});}
        else if (user) {
            if (user.admin_status) {
                next();
            } else {
                res.json({"response": 'You are not Authorize'});
            }
        }
    });

});

//listing API
appRouter.post('/listFeedback', function (req, res, next) {
    FeedbackModel.find((err, docs) => {
        if (!err) {
            res.render("feedbackList", {
                datas: docs,
                layout: ""
            });
        }
        else {
            res.json({'response': 'Request Failed'});
        }
    }).sort({'_id': -1});
});

//delete API
appRouter.post('/deleteFeedback', (req, res) => {
    FeedbackModel.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({'response': 'Feedback Deleted Successfully!!'});
        }
        else {
            res.json({'response': 'Error in delete :' + err});
        }
    });
});

//find Detail API
appRouter.post('/editFeedback', (req, res) => {
    FeedbackModel.findById(req.body._id, (err, data) => {
        if (!err) {
            res.json(data);
        }
    });
});

//Update API
appRouter.post('/updateFeedback', (req, res) => {
    FeedbackModel.findOneAndUpdate({_id: req.body._id}, req.body, {new: true}, (err, doc) => {
        if (!err) {
            res.json({'response': 'Success!!! Feedback is updated'});
        }
        else {
            res.json({'response': 'Error in Update : ' + err});
        }
    });
});


module.exports = appRouter;


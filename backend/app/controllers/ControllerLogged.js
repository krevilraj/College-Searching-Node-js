var exp = require('express');
var appRouter = exp.Router();
var UserModel = require('../models/UserModel');
var FeedbackModel = require('../models/ContactModel');
var CollegeEnrollModel = require('../models/CollegeEnrollModel');
var CollegeModel = require('../models/CollegeModel');
var CommentModel = require('../models/CommentModel');
var jsonToken = require('jsonwebtoken');
var bcrypt = require('bcrypt');
var dateFormat = require('dateformat');
var app = exp();


appRouter.use(function (req, res, next) {

    // check header or url parameters or post parameters for token
    var token = req.body.token || req.param('token') || req.headers['x-access-token'];

    // decode token
    if (token) {

        // verifies secret and checks exp
        jsonToken.verify(token, "cmessage", function (err, decoded) {
            if (err) {
                return res.json({success: false, message: 'Failed to authenticate token.'});
            } else {
                // if everything is good, save to request for use in other routes
                req.decoded = decoded;
                next();
            }
        });

    } else {

        // if there is no token
        // return an error
        return res.status(403).send({
            success: false,
            message: 'No token provided.'
        });

    }

});

appRouter.post('/profile_edit', (req, res) => {
    var all_data = req.body;
    all_data.password = bcrypt.hashSync(all_data.password, 10);
    all_data.repassword = all_data.password;
    UserModel.findOneAndUpdate({_id: req.body._id}, all_data, {new: true}, (err, doc) => {
        if (!err) {
            res.json({'response': 'Profile Updated Successfully!!', 'username': doc.username});
        }
        else {
            console.log('Error during record update : ' + err);
        }
    });
});

appRouter.post('/send_comment', (req, res) => {
    UserModel.findOne({
        _id: req.body.user_id
    }, function (err, user) {
        if (err) {
            res.json({'response': false,'message': 'Post Failed Something is wrong. Log in first!!1'});
        } else if (!user) {
            res.json({'response': false,'message': 'Post Failed Something is wrong. Log in first!!2'});
        } else if (user) {


            if (user.username == req.body.username) {
                var collegeComment = new CommentModel();

                collegeComment.college_id = req.body.college_id;
                collegeComment.comment = req.body.comment;
                collegeComment.username = req.body.username;

                collegeComment.save((err, doc) => {
                    if (err) {
                        console.log('Error during record insertion : ' + err);
                    } else {
                        res.json({'response': true,'message': 'Posted Successfully'});
                    }
                });
            } else {
                res.json({'response': false,'message': 'You are not Authorized login First'});
            }
        }

    });

});

appRouter.get('/users', function (req, res) {
    UserModel.find({}, function (err, users) {
        res.json(users);
    });
});

module.exports = appRouter;

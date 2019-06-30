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

appRouter.get('/homeCollege', function (req, res) {
    CollegeModel.find((err, docs) => {
        if (!err) {
            res.render("homeCollegePost", {
                datas: docs,
                layout: ""
            });
        }
        else {
            console.log('Error in retrieving page :' + err);
        }
    }).limit(8).sort({'_id': -1});
});

appRouter.get('/commentCollege/:id', function (req, res) {
    CommentModel.find({college_id: req.params.id}, (err, data) => {
        if (!err) {
            res.render("collegeComment", {
                datas: data,
                layout: ""
            });
        }
        else {
            console.log('Error in retrieving page :' + err);
        }
    }).sort({'_id': -1});
});

appRouter.get('/collegeDetail/:id', (req, res) => {
    CollegeModel.findById(req.params.id, (err, data) => {
        if (!err) {
            res.json(data);
        }
    });
});

appRouter.post('/college_search', (req, res) => {
    var s = req.body.query;
    var query = { college_name: new RegExp(s, 'i') };
    console.log(query);
    CollegeModel.find(query, (err, data) => {
        if (!err) {
            res.render("homeCollegePost", {
                datas: data,
                layout: ""
            });
        }
        else {
            console.log('Error in retrieving page :' + err);
        }
    });
});

appRouter.get('/college_list', function (req, res) {
    CollegeModel.find((err, docs) => {
        if (!err) {
            res.render("homeCollegePost", {
                datas: docs,
                layout: ""
            });
        }
        else {
            console.log('Error in retrieving page :' + err);
        }
    });
});

appRouter.post('/studentProfile', (req, res) => {
    UserModel.findOne({
        _id: req.body.id
    }, function (err, user) {
        if (err) {
            res.json({'response': 'Post Failed Something is wrong. Log in first!!1'});
        } else if (!user) {
            res.json({'response': 'Post Failed Something is wrong. Log in first!!2'});
        } else if (user) {

            if (user.username == req.body.username) {
                res.json(user);
            } else {
                res.json({'response': 'Login Failed!!'});
            }
        }
    });
});

//posting API
appRouter.post('/sendContact', (req, res) => {
    var newFeedback = new FeedbackModel({
        name: req.body.name,
        phone: req.body.phone,
        email: req.body.email,
        message: req.body.message
    });

    newFeedback.save(function (err, data) {
        if (err) {
            console.log('Failed to Add' + err);
        } else {
            res.json({'response': 'Success!!! Added to Feedback'});
        }
    });
});

//posting API
appRouter.post('/addCollegeEnroll', (req, res) => {
    var datetime = new Date();
    var newCollegeEnroll = new CollegeEnrollModel({
        name: req.body.name,
        email: req.body.email,
        phone: req.body.phone,
        message: req.body.message,
        college_id: req.body.college_id,
        date: dateFormat(new Date(), "d mmm, yyyy h:MM tt")
    });

    newCollegeEnroll.save(function (err, data) {
        if (err) {
            console.log('Failed to Add' + err);
        } else {
            res.json({'response': 'Success!!! Added to CollegeEnroll'});
        }
    });
});

//posting API
appRouter.post('/addCollegeComment', (req, res) => {
    var datetime = new Date();
    var newCollegeComment = new CommentModel({
        username: req.body.username,
        email: req.body.email,
        phone: req.body.phone,
        message: req.body.message,
        college_id: req.body.college_id,
        date: dateFormat(new Date(), "d mmm, yyyy h:MM tt")
    });

    newCollegeComment.save(function (err, data) {
        if (err) {
            console.log('Failed to Add' + err);
        } else {
            res.json({'response': 'Success!!! Comment Posted'});
        }
    });
});

appRouter.post('/login', function (req, res) {
    var response = res;
    console.log(req.body);
    // find the user
    UserModel.findOne({
        email: req.body.email
    }, function (err, data) {
        if (err) throw err;
        if (!data) {
            res.json({response: false, message: 'Student not found.'});
        } else if (data) {

            if (data.data_status == "Banned") {
                res.send({"response": false, message: "You are Banned"}); // send response to ajax call to view
            }
            else {

                bcrypt.compare(req.body.password, data.password, function (err, res) {
                    if (res) {
                        var token = jsonToken.sign({admin: data.admin_status}, "cmessage", {
                            expiresIn: 604800 // expires in 1 week
                        });

                        response.json({
                            response: true,
                            token: token,
                            username: data.username,
                            admin_status: data.admin_status,
                            _id: data._id,
                        });
                    } else {
                        response.json({"response": false, message: 'Wrong Password. Login Failed'});
                    }
                });
            }

        }

    });
});

appRouter.post('/register', function (req, res, next) {
    console.log(req.body);
    var student = req.body;
    if (!student.email || !student.username || !student.password || !student.repassword) {
        res.send({"response": "Complete all required Field"});
    } else {
        if (student.password == student.repassword) {
            UserModel.findOne({email: student.email}, function (err, data) {
                if (!data) {
                    var c;
                    // find the last user and take unique_id from that to variable c for new user
                    UserModel.findOne({}, function (err, data) {

                        var n_user = new UserModel({
                            email: student.email,
                            username: student.username,
                            password: bcrypt.hashSync(student.password, 10),
                            admin_status: false,
                            user_status: false,
                        });

                        n_user.save(function (err, Person) {
                            if (err)
                                console.log(err);
                            else
                                console.log('response');
                        });

                    }).sort({_id: -1}).limit(1);
                    res.send({"response": "Registered Successfully"}); // send response to ajax call to view
                } else {
                    res.send({"response": "Email is in use."}); // send response to ajax call to view
                }

            });
        } else {
            res.send({"response": "password is not matched"}); // send response to ajax call to view
        }
    }
});


module.exports = appRouter;

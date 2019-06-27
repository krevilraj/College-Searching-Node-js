var exp = require('express');
var appRouter = exp.Router();
var UserModel = require('../models/UserModel');
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
appRouter.post('/userList', function (req, res, next) {
    UserModel.find((err, docs) => {
        if (!err) {
            res.render("userList", {
                datas: docs,
                layout: ""
            });
        }
        else {
            res.json({'response': 'Request Failed'});
        }
    }).sort({'_id': -1});
});

//posting API
appRouter.post('/addUser', (req, res) => {
    UserModel.findOne({email: req.body.email}, function (err, data) {
        if (!data) {
            var newUser = new UserModel({
                name: req.body.full_name,
                phone: req.body.phone,
                email: req.body.email,
                username: req.body.username,
                password: bcrypt.hashSync(req.body.password, 10),
                admin_status: !!req.body.admin_status,
                user_status: !!req.body.user_status,
            });

            newUser.save(function (err, Person) {
                if (err) {
                    console.log('Failed to Add' + err);
                } else {
                    res.json({'response': 'Success!!! Added to user'});
                }
            });
        } else {
            res.send({"response": "Email is in use."});
        }
    });
});

//delete API
appRouter.post('/deleteUser', (req, res) => {
    UserModel.findByIdAndRemove(req.body.id, (err, doc) => {
        if (!err) {
            res.json({'response': 'User Deleted Successfully!!'});
        }
        else {
            res.json({'response': 'Error in delete :' + err});
        }
    });
});

//find Detail API
appRouter.post('/editUser', (req, res) => {
    UserModel.findById(req.body._id, (err, data) => {
        if (!err) {
            res.json(data);
        }
    });
});

//Update API
appRouter.post('/updateUser', (req, res) => {
    var request_data = req.body;
    request_data.password = bcrypt.hashSync(request_data.password, 10);
    request_data.name = req.body.full_name;
    UserModel.findOneAndUpdate({_id: req.body._id}, request_data, {new: true}, (err, doc) => { //find by id and update it
        if (!err) {
            res.json({'response': 'Success!!! User is updated'});
        }
        else {
            res.json({'response': 'Error in Update : ' + err});
        }
    });
});

module.exports = appRouter;

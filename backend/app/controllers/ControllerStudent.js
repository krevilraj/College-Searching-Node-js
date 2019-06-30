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
        if (err) {
            res.json({"response": false, "message": 'Something is wrong'});
        }
        else if (!user) {
            res.json({"response": false, "message": 'You are not Allowed'});
        }
        else if (user) {
            if (user.admin_status) {
                next();
            } else {
                res.json({"response": false, "message": 'You are not Authorize'});
            }
        }
    });

});

//posting API
appRouter.post('/addUser', (req, res) => {
    console.log(req.body);
    UserModel.findOne({email: req.body.email}, function (err, data) {
        if (!data) {
            if(req.body.admin_status=="true"){
                var admin_status = true;
            }else{
                var admin_status = false;
            }
            if(req.body.user_status=="true"){
                var user_status = true;
            }else{
                var user_status = false;
            }

            var newUser = new UserModel({
                name: req.body.name,
                phone: req.body.phone,
                email: req.body.email,
                username: req.body.username,
                password: bcrypt.hashSync(req.body.password, 10),
                admin_status: admin_status,
                user_status: user_status,
            });

            newUser.save(function (err, Person) {
                if (err) {
                    res.json({"response": false, 'message': err});
                } else {
                    res.json({"response": true, 'message': 'Success!!! Added to user'});
                }
            });
        } else {
            res.send({"response": false, "message": "Email is in use."});
        }
    });
});

//listing API
appRouter.post('/listUser', function (req, res, next) {
    UserModel.find((err, docs) => {
        if (!err) {
            res.render("userList", {
                datas: docs,
                layout: ""
            });
        }
        else {
            res.json({"response": false, 'message': 'Request Failed'});
        }
    }).sort({'_id': -1});
});


//delete API
appRouter.post('/deleteUser', (req, res) => {
    UserModel.findByIdAndRemove(req.body.id, (err, doc) => {
        if (!err) {
            res.json({"response": true, 'message': 'User Deleted Successfully!!'});
        }
        else {
            res.json({"response": false, 'message': 'Error in delete :' + err});
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
    if (req.body.password == "" || req.body.password == null) {
        if(req.body.admin_status=="true"){
            var admin_status = true;
        }else{
            var admin_status = false;
        }
        if(req.body.user_status=="true"){
            var user_status = true;
        }else{
            var user_status = false;
        }
        var request_data = {
            name: req.body.name,
            phone: req.body.phone,
            email: req.body.email,
            username: req.body.username,
            admin_status: admin_status,
            user_status: user_status,
        };
    } else {
        var request_data = req.body;
        request_data.password = bcrypt.hashSync(request_data.password, 10);
        request_data.admin_status = admin_status;
        request_data.user_status = user_status;

    }

    UserModel.findOneAndUpdate({_id: req.body._id}, request_data, {new: true}, (err, doc) => { //find by id and update it
        if (!err) {
            res.json({"response": true, 'message': 'Success!!! User is updated'});
        }
        else {
            res.json({"response": false, 'message': 'Error in Update : ' + err});
        }
    });
});

module.exports = appRouter;

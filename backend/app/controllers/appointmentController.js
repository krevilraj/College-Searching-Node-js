var express = require('express');
var router = express.Router();
var User = require('../models/user');
var jsonToken = require('jsonwebtoken');
var app = express();
var Appointment = require('../models/appointment');


router.use(function (req, res, next) {

    // check header or url parameters or post parameters for token
    var token = req.body.token || req.param('token') || req.headers['x-access-token'];
    // decode token
    if (token) {

        // verifies secret and checks exp
        jsonToken.verify(token, "cmessage", function (err, decoded) {
            if (err) {
                return res.json({success: false, message: 'Failed to authenticate token.'});
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
    console.log(req.body);
    User.findOne({
        _id: req.body.id
    }, function (err, user) {
        if (err) {
            res.json({'Success': 'Post Failed Something is wrong. Log in first!!'});
        } else if (!user) {
            res.json({'Success': 'User is not Allowed'});
        } else if (user) {
            if (user.admin) {
                next();
            } else {
                res.json({'Success': 'Authentication Failed!!'});
            }
        }

    });

});

router.post('/admin/add_appointment', (req, res) => {

    var appointment = new Appointment();            // Making Object of Appointment model

    appointment.name = req.body.name;               // put value of object attribute like name, petname, email, date, time, message
    appointment.petname = req.body.petname;
    appointment.phone = req.body.phone;
    appointment.email = req.body.email;
    appointment.date = req.body.date;
    appointment.time = req.body.time;
    appointment.message = req.body.message;


    appointment.save((err, doc) => {                // this save all value in object as well as in table also
        if (err) {
            console.log('Error during record insertion : ' + err);
        } else {
            res.json({'Success': 'Appointment added Successfully!!'});
        }
    });

});

router.post('/admin/list_appointment', function (req, res, next) {
    Appointment.find((err, docs) => {
        if (!err) {
            res.render("appointmentList", {      // Displaying view of list.hbs file containing HTML File
                appointments: docs,                 // Sending all appointment to view
                layout:""
            });
        }
        else {
            console.log('Error in retrieving employee list :' + err);
        }
    }).sort({'_id': -1});
});

//Url will be localhost:3000/appointment/delete/{appointment_id}
router.post('/admin/delete_appointment', (req, res) => {
    Appointment.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({'Success': 'Appointment added Successfully!!'});
        }
        else {
            console.log('Error in Appointment delete :' + err);
        }
    });
});

router.post('/admin/edit_appointment', (req, res) => {
    Appointment.findById(req.body._id, (err, doc) => {
        if (!err) {
            res.json(doc);
        }
    });
});

router.post('/admin/update_appointment', (req, res) => {
    Appointment.findOneAndUpdate({_id: req.body._id}, req.body, {new: true}, (err, doc) => { //find by id and update it
        if (!err) {
            res.json({'Success': 'Appointment Edited Successfully!!'});
        }
        else {
            console.log('Error during record update : ' + err);
            res.json({'Success': 'Failed :'+err});
        }
    });
});



module.exports = router;

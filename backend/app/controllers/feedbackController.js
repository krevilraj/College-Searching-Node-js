var express = require('express');
var router = express.Router();
var User = require('../models/user');
var jsonToken = require('jsonwebtoken');
var app = express();
var Feedback = require('../models/feedback');


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
        return res.json({
            success: false,
            message: 'No token provided.'
        });


    }
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
                res.json({'Success': 'Access Denied!!'});
            }
        }

    });

});



router.post('/admin/list_feedback', function (req, res, next) {
    Feedback.find((err, docs) => {
        if (!err) {
            res.render("feedbackList", {      // Displaying view of list.hbs file containing HTML File
                feedbacks: docs,                 // Sending all feedback to view
                layout:""
            });
        }
        else {
            res.json({'Success': 'Error in retriving'});
        }
    }).sort({'_id': -1});
});

//Url will be localhost:3000/feedback/delete/{feedback_id}
router.post('/admin/delete_feedback', (req, res) => {
    Feedback.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({'Success': 'Feedback added Successfully!!'});
        }
        else {
            console.log('Error in Feedback delete :' + err);
        }
    });
});

router.post('/admin/view_feedback', (req, res) => {
    Feedback.findById(req.body._id, (err, doc) => {
        if (!err) {
            res.json(doc);
        }
    });
});

router.post('/admin/update_feedback', (req, res) => {
    Feedback.findOneAndUpdate({_id: req.body._id}, req.body, {new: true}, (err, doc) => {
        if (!err) {
            res.json({'Success': 'Feedback Edited Successfully!!'});
        }
        else {
            console.log('Error during record update : ' + err);
            res.json({'Success': 'Failed :'+err});
        }
    });
});



module.exports = router;

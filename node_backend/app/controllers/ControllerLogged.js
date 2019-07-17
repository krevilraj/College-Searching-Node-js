var exp = require('express');
var appRouter = exp.Router();
var UserModel = require('../models/UserModel');
var FeedbackModel = require('../models/ContactModel');
var CollegeEnrollModel = require('../models/CollegeEnrollModel');
var CollegeModel = require('../models/CollegeModel');
var RatingModel = require('../models/RatingModel');
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

appRouter.post('/updateUser', (req, res) => {
    if (req.body.password == "" || req.body.password == null) {
        if(req.body.admin_status=="true"){
            var admin_status = true;
        }else{
            var admin_status = false;
        }
        var request_data = {
            name: req.body.name,
            phone: req.body.phone,
            email: req.body.email,
			image: req.body.image,
            username: req.body.username,
        };
    } else {
        var request_data = {
            name: req.body.name,
            phone: req.body.phone,
            email: req.body.email,
            username: req.body.username,
        };
        request_data.password = bcrypt.hashSync(req.body.password, 10);

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

//find Detail API
appRouter.post('/editUser', (req, res) => {
    UserModel.findById(req.body._id, (err, data) => {
        console.log('test');
        if (!err) {
            res.json(data);
        }else{
            console.log('error:'+err);
        }
    });
});

// College CRUD
//insert API
appRouter.post('/addCollege', (req, res) => {
    var newCollege = new CollegeModel({
        college_name: req.body.college_name,
        location: req.body.location,
        website: req.body.website,
        phone: req.body.phone,
        course: req.body.course,
        image: req.body.image,
        lat: req.body.lat,
        lon: req.body.lon,
		price: req.body.price,
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

//insert API
appRouter.post('/addReview', (req, res) => {
    var rating = new RatingModel({
        college_id: req.body.college_id,
        user: req.body.user,
        review: req.body.review,
        rating: req.body.rating
    });

    rating.save(function (err, data) {
        if (err) {
            res.json({"response": false,'message': err});
        } else {
            res.json({"response": true,'message': 'Success!!! Review Added'});
        }
    });
});



//listing API
appRouter.post('/listCollege', function (req, res, next) {
    CollegeModel.find((err, docs) => {
        if (!err) {
            res.json({"response": false,'data': docs});
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
appRouter.post('/collegeDetail', (req, res) => {
    CollegeModel.findById(req.body.college_id, (err, data) => {
        if (!err) {
            var e_rating = 0;
            var rating  = 0;
            RatingModel.find({college_id: req.body.college_id}, (err, doc) => {

                if (!err) {

                    doc.forEach(function(element) {
                        rating += element.rating;
                    });
                    e_rating = rating/5;
                    Object.assign(data, {rating: e_rating});
                     res.json(data);
                }
                else {
                    console.log('Error in retrieving page :' + err);
                }
            });

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

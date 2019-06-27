var express = require('express');
var router = express.Router();
var User = require('../models/user');
var jsonToken = require('jsonwebtoken');
var app = express();
var Forum = require('../models/forum');


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

router.post('/admin/add_forum', (req, res) => {

    var forum = new Forum();            // Making Object of forum model

    forum.title = req.body.title;
    forum.description = req.body.description;
    forum.author = req.body.username;

    forum.save((err, doc) => {                // this save all value in object as well as in table also
        if (err) {
            console.log('Error during record insertion : ' + err);
        } else {
            res.json({'Success': 'Forum added Successfully!!'});
        }
    });

});

router.post('/admin/list_forum', function (req, res, next) {
    Forum.find((err, docs) => {
        if (!err) {
            res.render("forumList", {      // Displaying view of list.hbs file containing HTML File
                forums: docs,                 // Sending all forum to view
                layout:""
            });
        }
        else {
            res.json({'Success': 'Forum added Successfully!!'});
        }
    }).sort({'_id': -1});
});

//Url will be localhost:3000/forum/delete/{forum_id}
router.post('/admin/delete_forum', (req, res) => {
    Forum.findByIdAndRemove(req.body._id, (err, doc) => {
        if (!err) {
            res.json({'Success': 'Forum added Successfully!!'});
        }
        else {
            res.json({'Success': 'Failed!!!'});
        }
    });
});

router.post('/admin/edit_forum', (req, res) => {
    Forum.findById(req.body._id, (err, doc) => {
        if (!err) {
            res.json(doc);
        }
    });
});

router.post('/admin/update_forum', (req, res) => {
    console.log('test->');
    console.log(req.body);
    Forum.findOneAndUpdate({_id: req.body._id}, req.body, {new: true}, (err, doc) => { //find by id and update it
        if (!err) {
            res.json({'Success': 'Forum Edited Successfully!!'});
        }
        else {
            console.log('Error during record update : ' + err);
            res.json({'Success': 'Failed :'+err});
        }
    });
});



module.exports = router;

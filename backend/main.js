var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var morgan = require('morgan');
const path = require('path');
const database = "CollegeSearching"


var UserModel = require('./app/models/user'); // get our mongoose model
const e_handlebar = require('express-handlebars');
var cors = require('cors');



app.set('views', path.join(__dirname, '/app/response_layouts/'));
app.engine('hbs', e_handlebar({
    extname: 'hbs',
    defaultLayout: 'mainLayout',
    helpers: {
        ifEqual: function (v1, v2, options) {
            if (v1 === v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        },
        ifNotEqual: function (v1, v2, options) {
            if (v1 != v2) {
                return options.fn(this);
            }
            return options.inverse(this);
        },
        plusOne: function (v1) {
            return v1 + 1;
        },
        trimString: function (passedString) {
            var StrippedString = passedString.replace(/(<([^>]+)>)/ig, "");
            var theString = StrippedString.substring(0, 150);
            return theString;
        },
        trimTitle: function (passedString) {
            var StrippedString = passedString.replace(/(<([^>]+)>)/ig, "");
            var theString = StrippedString.substring(0, 35);
            return theString;
        }
    }
}));
app.set('view engine', 'hbs');

app.use(bodyParser.urlencoded({extended: false}));
app.use(bodyParser.json());
app.use(morgan('dev'));

var port = process.env.PORT || 8080; // used to create, sign, and verify tokens
// connect to database
mongoose.connect("mongodb://localhost:27017/" + database, {useNewUrlParser: true}, (err) => {
    if (!err) {
        console.log('Connected Successfully')
    }
    else {
        console.log('Error in Connection: ' + err)
    }

});


const homeController = require('./app/controllers/ControllerHome');
const studentController = require('./app/controllers/ControllerStudent');
const feedbackController = require('./app/controllers/ControllerFeedback');
const collegeController = require('./app/controllers/ControllerCollege');
const collegeEnrollController = require('./app/controllers/ControllerCollegeEnroll');


const loginController = require('./app/controllers/loginController');
const frontendController = require('./app/controllers/frontendController');
const appointmentController = require('./app/controllers/appointmentController');
const forumController = require('./app/controllers/forumController');
// const feedbackController = require('./app/controllers/feedbackController');
const userController = require('./app/controllers/ControllerStudent');


app.get('/setup', function (req, res) {

    // create a sample user
    var nick = new UserModel({
        name: 'Nick Cerminara',
        password: 'password',
        admin: true
    });
    nick.save(function (err) {
        if (err) throw err;

        console.log('UserModel saved successfully');
        res.json({success: true});
    });
});

// basic route (http://localhost:8080)
app.get('/', function (req, res) {
    res.send('Hello! The API is at http://localhost:' + port + '/api');
});


var apiRoutes = express.Router();

var corsOptions = {
    'allowedHeaders': ['sessionId', 'Content-Type'],
    'exposedHeaders': ['sessionId'],
    "origin": "*",
    "methods": "GET,HEAD,PUT,PATCH,POST,DELETE",
    "preflightContinue": false,
    "optionsSuccessStatus": 204
};

app.use(express.static(__dirname + '/images'));
app.use(cors(corsOptions));
app.use('/api', cors(corsOptions), frontendController);
app.use('/api', cors(corsOptions), loginController);
app.use('/api', cors(corsOptions), appointmentController);
app.use('/api', cors(corsOptions), forumController);

app.use('/', homeController);
app.use('/admin', studentController);
app.use('/admin', feedbackController);
app.use('/admin', collegeController);
app.use('/admin', collegeEnrollController);


app.listen(port);
console.log('Server runs at http://localhost:' + port);

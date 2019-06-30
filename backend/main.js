var express = require('express');
var app = express();
var bodyParser = require('body-parser');
var mongoose = require('mongoose');
var morgan = require('morgan');
const path = require('path');
const database = "CollegeSearching"

const e_handlebar = require('express-handlebars');
var cors = require('cors');



app.set('views', path.join(__dirname, '/app/response_layouts/'));
app.engine('hbs', e_handlebar({
    extname: 'hbs',
    defaultLayout: 'mainLayout',
    helpers: {
        plusOne: function (v1) {
            return v1 + 1;
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
const loggedController = require('./app/controllers/ControllerLogged');
const studentController = require('./app/controllers/ControllerStudent');
const feedbackController = require('./app/controllers/ControllerContact');
const collegeController = require('./app/controllers/ControllerCollege');
const collegeEnrollController = require('./app/controllers/ControllerCollegeEnroll');

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

app.use('/', homeController);
app.use('/student', loggedController);
app.use('/admin', studentController);
app.use('/admin', feedbackController);
app.use('/admin', collegeController);
app.use('/admin', collegeEnrollController);


app.listen(port);
console.log('Server runs at http://localhost:' + port);

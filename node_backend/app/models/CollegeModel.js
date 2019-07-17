var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('Colleges', new Schema({
    college_name: String,
    location: String,
    website: String,
    phone: String,
    course: String,
    description: String,
    rating: Number,
    lat: Number,
    lon: Number,
	price: Number,
    image:String,
}));

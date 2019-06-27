var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('Colleges', new Schema({
    college_name: String,
    location: String,
    website: String,
    phone: String,
    image: String,
    description: String
}));

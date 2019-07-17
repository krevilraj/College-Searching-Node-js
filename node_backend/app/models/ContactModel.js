var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('Feedbacks', new Schema({
    name: String,
    email: String,
    phone: String,
    message: String
}));

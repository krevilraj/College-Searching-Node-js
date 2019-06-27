var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('CollegeEnrolls', new Schema({
    name: String,
    email: String,
    phone: String,
    message: String,
    college_id: Schema.Types.ObjectId,
    date: String
}));
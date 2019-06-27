var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('CollegeComment', new Schema({
    username: String,
    comment: String,
    college_id: Schema.Types.ObjectId,
    date: String
}));
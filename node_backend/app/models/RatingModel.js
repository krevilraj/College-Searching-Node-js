var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('CollegeRating', new Schema({
    rating: Number,
    user: String,
    review: String,
    college_id: Schema.Types.ObjectId
}));
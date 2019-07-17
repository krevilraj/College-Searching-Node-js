var mongoose = require('mongoose');
var Schema = mongoose.Schema;
module.exports = mongoose.model('Users', new Schema({
    name: String,
    admin_status: Boolean,
    email: String,
    phone: String,
    username: String,
    password: String,
    user_status: Boolean,
}));

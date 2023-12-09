const mongoose = require('mongoose');
const { Schema } = mongoose;

const hashtagSchema = new Schema({
    tagName : {
        type : String,
        required : false,
    },
    count : {
        type : Number,
        required : false,
        default : 1,
    }
}, {
    versionKey : false
})

module.exports = mongoose.model('Hashtag', hashtagSchema);
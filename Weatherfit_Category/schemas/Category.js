const mongoose = require('mongoose');
const { Schema } = mongoose;

const categorySchema = new Schema({
    categoryName : {
        type : String,
        required : false,
    },
    temperature : {
        type : Number,
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

module.exports = mongoose.model('Category', categorySchema);
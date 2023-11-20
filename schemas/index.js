require('dotenv').config({ path: '/app/env' });
// require('dotenv').config();

const mongoose = require('mongoose');

async function connectDB() {
    try {
        await mongoose.connect(`mongodb+srv://${process.env.MONGODB_USERNAME}:${process.env.MONGODB_PASSWORD}@cluster0.svdkiun.mongodb.net/category`);
        console.log('몽고db 연결..');
    } catch (err) {
        console.log(err.message);
        process.exit(1);
    }
}

module.exports = { connectDB };
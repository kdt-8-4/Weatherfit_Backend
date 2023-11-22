require('dotenv').config({ path: '/app/env' });
// require('dotenv').config();

const kafkaBrokers = process.env.KAFKA_BROKERS;
const { Kafka } = require('kafkajs');
const Category = require('../schemas/Category');
const Hashtag = require('../schemas/Hashtag');

exports.getHashtagsInfo = async (req, res) => {
    try {
        const result = await Hashtag.find().sort('-count');

        res.json({ result });
    } catch (err) {
        const message = `${err.name} : ${err.parent}`;
        res.status(500).json({ message })
    }
}

exports.getHashtagInfo = async (req, res) => {
    const tagName = req.query.tagName;
    try {
        const result = await Hashtag.findOne({ tagName : tagName });

        res.json({ result });
    } catch (err) {
        const message = `${err.name} : ${err.parent}`;
        res.status(500).json({ message })
    }
}

exports.getTop5 = async (req, res)=> {
    const temp_min = parseFloat(req.query.temp_min);
    const temp_max = parseFloat(req.query.temp_max);
    const temp = Math.round((temp_min + temp_max) / 2);
    const minus = temp - 1;
    const plus = temp + 1;

    try {
        const result = await Category.aggregate([
            {
                $match: {
                    temperature: { $gte: minus, $lte: plus }
                }
            },
            {
                $group: {
                    _id: "$categoryName",
                    count: { $sum: "$count" }
                }
            },
            {
                $sort: { count: -1 }
            },
            {
                $limit: 5
            }
        ]);
        res.json({ result });
    } catch(err) {
        const message = `${err.name} : ${err.parent}`;
        res.status(500).json({ message })
    }
}

exports.searchByTyping = async (req, res) => {
    const text = req.query.text;

    try {
        const result = await Hashtag.find({
            tagName: { $regex: text, $options: 'i' }
        }).sort({ count: -1 })

        console.log(result);
        res.json({ result });
    } catch (err) {
        const message = `${err.name} : ${err.parent}`;
        res.status(500).json({ message })
    }
}

const kafka = new Kafka({
    clientId : 'category_service',
    brokers : [kafkaBrokers]
})

const consumer = kafka.consumer({ groupId : 'cate'});
const update = { $setOnInsert : { count : 0 } };
const options = { upsert : true };

exports.initKafka = async () => {
    await consumer.connect();
    await consumer.subscribe({ topic : 'category', fromBeginning : true })
    await consumer.subscribe({ topic : 'hashtag', fromBeginning : true })
    await consumer.run({
        eachMessage : async ({ topic, partition, message }) => {
            const replace = message.value.toString().replaceAll('"', '');
            //카테고리
            if(topic === "category") {
                console.log('-----------------------카테고리---------------------');
                const temperature = replace.split('/')[0];
                console.log("temperature : ", temperature);
                const categories = replace.split('/');
                for(let i=1 ; i<categories.length ; i++) {
                    console.log(categories[i]);
                    const query = { categoryName : categories[i], temperature : temperature };

                    const insertOrUpdate = await Category.updateOne(query, update, options);
                    const updateCount = { $inc : { count : 1 } };
                    const again = await Category.updateOne(query, updateCount);
                }
            } else {    //해시태그
                console.log('----------------------해시태그----------------------');
                const hashtags = replace.split('/');
                for(let i=0 ; i<hashtags.length ; i++) {
                    const query = { tagName : hashtags[i] };

                    const insertOrUpdate = await Hashtag.updateOne(query, update, options);
                    const updateCount = { $inc : { count : 1 } };
                    const again = await Hashtag.updateOne(query, updateCount);
                }
            }

        }
    })
}

require('dotenv').config({ path: '/app/env' });
// require('dotenv').config();

const kafkaBrokers = process.env.KAFKA_BROKERS;
const { Kafka } = require('kafkajs');
const Category = require('../schemas/Category');
const Hashtag = require('../schemas/Hashtag');
exports.test = async (req ,res) => {
    const categoryName = req.query.categoryName;
    const temperature = Number(req.query.temperature);
    console.log('categoryName : ', categoryName);
    try {
        const result = await Category.updateOne(
            { categoryName : categoryName, temperature: temperature },
            { $inc : { count : -1 } }
        );

        res.json({ result });
    } catch (err) {
        const message = `${err.name} : ${(parent)}`;
        res.status(500).json({ message });
    }
}
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
                $match: { temperature: { $gte: minus, $lte: plus } }
            },
            {
                $group: { _id: "$categoryName", count: { $sum: "$count" } }
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
            console.log({
                topic,
                partition,
                offset: message.offset,
                value: message.value.toString(),
            })
            //카테고리
            const values = message.value.toString().replaceAll('"', '');
            if(topic === "category") {
                console.log('-----------------------카테고리---------------------');
                if(partition == 0) {
                    console.log('카테고리 파티션 1');
                    insertCategories(values);
                } else if(partition == 1) {
                    console.log('카테고리 파티션 2');
                    updateCategories(values);
                } else {
                    console.log('카테고리 파티션 3');
                    deleteCategories(values);
                }
            } else {    //해시태그
                console.log('----------------------해시태그----------------------');
                if(partition == 0) {
                    console.log('해시태그 파티션 1');
                    insertHashtags(values);
                } else if(partition == 1) {
                    console.log('해시태그 파티션 2');
                    updateHashtags(values);
                } else {
                    console.log('해시태그 파티션 3');
                    deleteHashtags(values);
                }
            }

        }
    })
}
async function insertCategories(values) {
    const temperature = values.split('/')[0];
    const categories = values.split('/');
    for(let i=1 ; i<categories.length ; i++) {
        console.log(categories[i]);
        const query = {categoryName: categories[i], temperature: temperature};

        const insertOrUpdate = await Category.updateOne(query, update, options);
        const updateCount = {$inc: {count: 1}};
        const again = await Category.updateOne(query, updateCount);
    }
}

async function insertHashtags(values) {
    const hashtags = values.split('/');
    for(let i=0 ; i<hashtags.length ; i++) {
        const query = { tagName : hashtags[i] };

        const insertOrUpdate = await Hashtag.updateOne(query, update, options);
        const updateCount = { $inc : { count : 1 } };
        const again = await Hashtag.updateOne(query, updateCount);
    }
}
async function deleteCategories(values) {
    const temperature = values.split('/')[0];
    const categories = values.split('/');
    for(let i=1 ; i<categories.length ; i++) {
        const result = await Category.updateOne(
            { categoryName : categories[i], temperature: temperature },
            { $inc : { count : -1 } }
        );
    }
}

async function updateCategories(values) {
    const arr = values.split(':');
    const temperature = arr[0].split('/')[0];
    const beforeCategories = arr[0].split('/');
    const afterCategories = arr[1].split('/');

    //전에 사용한 카테고리 delete
    for(let i=1 ; i<beforeCategories.length ; i++) {
        const result = await Category.updateOne(
            { categoryName : beforeCategories[i], temperature: temperature },
            { $inc : { count : -1 } }
        );
    }
    //수정한 카테고리 insert
    for(let i=0 ; i<afterCategories.length ; i++) {
        const query = { categoryName : afterCategories[i], temperature : temperature };

        const insertOrUpdate = await Category.updateOne(query, update, options);
        const updateCount = { $inc : { count : 1 } };
        const again = await Category.updateOne(query, updateCount);
    }
}

async function deleteHashtags(values) {
    const hashtags = values.split('/');
    for(let i=0 ; i<hashtags.length ; i++) {
        const result = await Hashtag.updateOne(
            { tagName: hashtags[i] },
            { $inc: { count: -1 } }
        )
    }
}

async function updateHashtags(values) {
    const arr = values.split(':');
    const beforeHashtags = arr[0].split('/');
    const afterHashtags = arr[1].split('/');

    //전에 사용했던 해시태그 삭제
    for(let i=0 ; i<beforeHashtags.length ; i++) {
        const result = await Hashtag.updateOne(
            { tagName: beforeHashtags[i] },
            { $inc: { count: -1 } }
        )
    }

    //수정한 해시태그 추가
    for(let i=0 ; i<afterHashtags.length ; i++) {
        const query = { tagName : afterHashtags[i] };

        const insertOrUpdate = await Hashtag.updateOne(query, update, options);
        const updateCount = { $inc : { count : 1 } };
        const again = await Hashtag.updateOne(query, updateCount);
    }
}
const models = require('../models/index');
const {QueryTypes} = require("sequelize");
const { sequelize } = require('../models/index')

exports.getHashtagInfo = async (req, res) => {
    try {
        const result = await models.Hashtag.findAll();
        console.log(result);
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
    const query = `SELECT categoryName, sum(count) as count FROM category WHERE temperature >= ? AND temperature <= ? GROUP BY categoryName ORDER BY count DESC LIMIT 5`;

    try {
        const result = await sequelize.query(query, {
            type : QueryTypes.SELECT,
            replacements : [minus, plus],
        });

        let categorys = new Array();
        result.forEach((val)=> {
           categorys.push(val.categoryName);
        });

        res.json({ categorys });
    } catch(err) {
        const message = `${err.name} : ${err.parent}`;
        res.status(500).json({ message })
    }
}
var express = require('express');
var router = express.Router();
const CategoryController = require('../controller/CategoryController');

/* GET home page. */
router.get('/hashtags', CategoryController.getHashtagInfo);

router.get('/tops', CategoryController.getTop5);


module.exports = router;

var express = require('express');
var router = express.Router();
const CategoryController = require('../controller/CategoryController');

/* GET home page. */
router.get('/hashtags', CategoryController.getHashtagsInfo);
router.get('/hashtag', CategoryController.getHashtagInfo);
router.get('/typing', CategoryController.searchByTyping);
router.get('/tops', CategoryController.getTop5);
module.exports = router;

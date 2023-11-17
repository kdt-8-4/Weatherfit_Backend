// require('dotenv').config();
require('dotenv').config({ path: '/app/env' });
const env = process.env;


const production = {
  username: env.MYSQL_USERNAME,
  password: env.MYSQL_PASSWORD,
  database: env.MYSQL_DATABASE,
  host: env.MYSQL_HOST,
  dialect: 'mysql',
};

module.exports = { production };

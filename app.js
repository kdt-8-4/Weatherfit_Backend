var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var app = express();

// require('dotenv').config();
require('dotenv').config({ path: '/app/env' });

app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));
app.use(cookieParser());
app.use(express.static(path.join(__dirname, 'public')));

app.use('/', indexRouter);
app.use('/users', usersRouter);

const Eureka = require('eureka-js-client').Eureka;

// const client = new Eureka({
//     // Eureka 서버의 설정
//     eureka: {
//         host: process.env.EUREKA_HOST,  // Eureka 서버의 주소
//         port: process.env.EUREKA_PORT,  // Eureka 서버의 포트
//         servicePath: '/eureka/apps/'
//     },
//     instance: {
//         instanceId : 'category-service',
//         app: 'category-service',  // 서비스의 이름
//         hostName: `${process.env.INSTANCE_HOSTNAME}`,
//         ipAddr: `${process.env.INSTANCE_IPADDRESS}`,  // 서비스의 IP 주소
//         port: {
//             '$': process.env.PORT,  // 서비스의 포트
//             '@enabled': 'true',
//         },
//         vipAddress: 'category-service',  // 서비스의 VIP 주소
//         statusPageUrl: `http://${process.env.INSTANCE_IPADDRESS}:${process.env.PORT}`,  // 서비스의 상태 페이지 URL
//         healthCheckUrl: `http://${process.env.INSTANCE_IPADDRESS}:${process.env.PORT}/health`,  // 서비스의 상태 페이지 URL
//         dataCenterInfo: {
//             '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
//             name: 'MyOwn',
//         },
//     },
// });
// client.start();  // Eureka 서버에 서비스를 등록
app.get('/health', (req, res) => {
    res.json({status: 'UP'});
});

app.get('/nodetest', (req, res)=> {
    res.json({ check : 'check' });
})

const {connectDB} = require('./schemas/index')
connectDB();
const {initKafka} = require('./controller/CategoryController.js');

app.listen(process.env.PORT, () => {
    initKafka().catch(console.error);
    console.log(`http://localhost:${process.env.PORT}`);
});


module.exports = app;

const { DataTypes } = require("sequelize");

const Model = (sequelize) => {
    return sequelize.define("category", {
        id: {
            type: DataTypes.INTEGER,
            allowNull: false,
            primaryKey: true,
            autoIncrement: true,
        },
        categoryName: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        temperature: {
            type: DataTypes.INTEGER,
            allowNull: false,
        },
        count : {
            type : DataTypes.INTEGER,
            allowNull : true,
            defaultValue : 0,
        }
    }, {
        freezeTableName : true,
        timestamps : false
    });
};

module.exports = Model;

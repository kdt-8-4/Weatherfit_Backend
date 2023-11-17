const { DataTypes } = require("sequelize");

const Model = (sequelize) => {
    return sequelize.define("hashtag", {
        id: {
            type: DataTypes.INTEGER,
            allowNull: false,
            primaryKey: true,
            autoIncrement: true,
        },
        tagName: {
            type: DataTypes.STRING,
            allowNull: false,
        },
        count: {
            type: DataTypes.INTEGER,
            allowNull: true,
            defaultValue : 0
        },
    }, {
        freezeTableName : true,
        timestamps : false
    });
};

module.exports = Model;

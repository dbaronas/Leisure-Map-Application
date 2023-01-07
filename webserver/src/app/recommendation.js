const pcc = require("calculate-correlation")
const Pool = require('pg').Pool
const pool = require('./database')
const getRecommendation = (req, res) => {
    const username = req.query.username
    let userArray = []
    let similarUserArray = []
    let mostSimilar = {
        user: '',
        coefficient: null
    }
    let max = -2
    pool.query(`SELECT * FROM getTags WHERE username='${username}'`, (error, results) => {
        if(error){
            throw error
        }
        for(let i = 0; i <= 44; i++){
            userArray[i] = results.rows[0][`${i + 1}`]
        }
        pool.query(`SELECT * FROM getTags WHERE username NOT IN ('${username}')`, (error, results2) => {
            if(error){
                throw error
            }
            for(let i = 0; i < results2.rows.length; i++){
                for(let y = 0; y <= 44; y++){
                    similarUserArray[y] = results2.rows[i][`${y + 1}`]
                }
                let correlation = pcc(userArray, similarUserArray)
                    if(correlation > max){
                        max = correlation
                        mostSimilar.user = results2.rows[i].username
                        mostSimilar.coefficient = correlation
                    }
            }
            res.send({mostSimilar})
        })
    })
}

module.exports = {
    getRecommendation,
}
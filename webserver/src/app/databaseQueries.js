const Pool = require('pg').Pool
const pool = require('./database')
const getTable = (req, res) => {
  const name = req.query.name
  pool.query(`SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = '${name}')`, (error, results) => {
    if(results.rows[0].exists == false){
      res.json({ERROR: `Table ${name} does not exist`})
    }
    else{
      pool.query(`SELECT * FROM ${name}`, (error, results) => {
        if (error) {
          res.send(err)
        }
        else{
        res.send({Table: results.rows})
        } 
      })
    }
  })
}

const savePlace = (req, res) => {
  const username = req.query.username
  const id = req.query.id

  pool.query(`INSERT INTO favourite_places (username, place_id) VALUES ($1, $2)`, [username, id], (error, results) => {
    if(error){
      throw error
    }
    const STATUS = {
      STATUS: `Place saved successfully!`
    }
    res.send({STATUS})
  })
}

/*const searchSession = (req, res) => {
  const username = req.body.username
  const start = req.body.start
  const end = req.body.end
  const places = req.body.places

  pool.query(`INSERT INTO sessions (username, start_time, end_time) VALUES ($1, $2, $3)`, [username, start, end], (error, results) => {
    if(error){
      throw error
    }
  })
  pool.query(`SELECT session_id FROM sessions WHERE username='${username}' ORDER BY session_id DESC `)
  for(let i = 0; i < places.lenght; i++){
    pool.query(`INSERT INTO`)
  }
}*/

module.exports = {
  getTable,
  savePlace,
  //searchSession
}

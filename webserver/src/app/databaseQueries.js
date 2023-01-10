const pool = require('./database')
const math = require('exact-math')
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

const getView = (req, res) => {
  const name = req.query.name
  pool.query(`SELECT EXISTS (SELECT FROM pg_views WHERE schemaname = 'public' AND viewname = '${name}')`, (error, results) => {
    if(results.rows[0].exists == false){
      res.json({ERROR: `View ${name} does not exist`})
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

const ratePlace = (req, res) => {
  const id = req.query.id
  const rating = req.query.rating

  pool.query(`SELECT * FROM place_rating WHERE place_id=${id}`, (error, results) => {
    if(error){
      throw error
    }

    const newRating = math.div(math.add(math.mul(results.rows[0].rating, results.rows[0].count), rating), results.rows[0].count + 1)

    pool.query(`UPDATE place_rating SET rating=${newRating}, count=${results.rows[0].count + 1} WHERE place_id=${id}`, (error) => {
      if(error){
        throw error
      }
      const STATUS = `Thank you fro your feedback!`
      res.send({STATUS})
    })
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
  getView,
  savePlace,
  ratePlace
  //searchSession
}

const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '193.219.91.103',
  port: 9170,
  database: 'pblDB',
  password: 'admin'
})
const getTable = (req, res) => {
  const name = req.query.name
  pool.query(`SELECT EXISTS (SELECT FROM pg_tables WHERE schemaname = 'public' AND tablename = '${name}')`, (error, results) => {
    if(results.rows[0].exists == false){
      res.status(404).json({ERROR: `Table ${name} does not exist`})
    }
    else{
      pool.query(`SELECT * FROM ${name}`, (error, results) => {
        if (error) {
          res.status(404).send(err)
        }
        else{
        res.status(200).send({Table: results.rows})
        } 
      })
    }
  })
}

module.exports = {
  getTable,
}

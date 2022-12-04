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
      res.json({ERROR: `Table ${name} does not exist`})
    }
    else{
      pool.query(`SELECT * FROM ${name} ORDER BY name ASC`, (error, results) => {
        if (error) {
          throw error
        }
    const Project = {
        Project_name: 'Maps.Relax',
        Team: 'Kernel Technologies'
        
    }
        res.status(200).send({Project, Table: results.rows})
      })
    }
  })
}

module.exports = {
  getTable,
}

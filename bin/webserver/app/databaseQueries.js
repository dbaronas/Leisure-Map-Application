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
      res.send('Table ' + name + ' does not exist')
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

const createUser = (req, res) => {
  const { name, surname } = req.body

  pool.query('INSERT INTO users (name, surname) VALUES ($1, $2)', [name, surname], (error, results) => {
    if (error) {
      throw error
    }
    res.status(201).send(`User added with ID: ${results.insertId}`)
  })
}

const updateUser = (req, res) => {
  const id = parseInt(req.params.id)
  const { name, surname } = req.body

  pool.query(
    'UPDATE users SET name = $1, surname = $2 WHERE id = $3',
    [name, surname, id],
    (error, results) => {
      if (error) {
        throw error
      }
      res.status(200).send(`User modified with ID: ${id}`)
    }
  )
}

const deleteUser = (req, res) => {
  const id = parseInt(req.params.id)

  pool.query('DELETE FROM users WHERE id = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    res.status(200).send(`User deleted with ID: ${id}`)
  })
}

module.exports = {
  getTable,
  createUser,
  updateUser,
  deleteUser,
}

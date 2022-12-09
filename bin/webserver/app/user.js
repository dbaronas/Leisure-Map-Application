const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '193.219.91.103',
  port: 9170,
  database: 'pblDB',
  password: 'admin'
})

const login = (req, res) => {
    const username = req.query.username
    const pass = req.query.pass

    pool.query(`SELECT EXISTS(SELECT 1 FROM users WHERE username='${username}')`, (error, results) => {
        if(results.rows[0].exists == true){
            pool.query(`SELECT password FROM users WHERE username='${username}'`, (error, results) => {
            if(results.rows[0].password == pass){
                const STATUS = {
                    LOGIN: true
                }
                res.send({STATUS})
            }
             else {
                const STATUS = {
                LOGIN: false
             }
                res.send({STATUS})
        }
    })
        }
        else{
            const STATUS = {
                LOGIN: false,
                message: 'User does not exist'
            }
            res.status(404).send({STATUS})
        }
    })
}

const createUser = (req, res) => {
    const username = req.query.username
    const pass = req.query.pass


  pool.query(`SELECT EXISTS(SELECT 1 FROM users WHERE username='${username}')`, (error, results) => {
        if(results.rows[0].exists == false){
        pool.query('INSERT INTO users (username, password) VALUES ($1, $2)', [username, pass], (error, results) => {
            if (error) {
              res.status(404).send(error)
            }
            else{
              res.status(200).json({STATUS: 'Account successfully created'})
            }
          })
        }
        else{
          res.status(404).json({STATUS: 'Account with that username already exists'})
        }})

  }
  
const updateUser = (req, res) => {
    const username = req.query.username
    const newpass = req.query.newpass
  
    pool.query(
      'UPDATE users SET password = $1 WHERE username = $2',
      [newpass, username],
      (error, results) => {
        if (error) {
          res.status(404).send(error)
        }
        else{
          res.status(200).json({STATUS: 'Password updated successfully'})
        }
      }
    )
}
  
const deleteUser = (req, res) => {
    const username = req.query.username
  
    pool.query('DELETE FROM users WHERE username = $1', [username], (error, results) => {
      if (error) {
        res.status(404).send(error)
        return
      }
      res.status(200).json({STATUS: 'Account deleted successfully'})
    })
}

module.exports = {
    login,
    createUser,
    updateUser,
    deleteUser,
}
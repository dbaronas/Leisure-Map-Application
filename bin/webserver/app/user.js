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
            res.send({STATUS})
        }
    })
}

const createUser = (req, res) => {
    /*const username = req.query.username
    const pass = req.query.pass*/


    for(let i = 1; i <= 100000; i++){
        var username = `labas${i}`
        var pass = `labas${i}`
        pool.query('INSERT INTO users (username, password) VALUES ($1, $2)', [username, pass], (error, results) => {
            if (error) {
              throw error
            }
            //res.status(201).json({STATUS: 'Account successfully created'})
          })
    }
    res.send('done')

  }
  
const updateUser = (req, res) => {
    const username = req.query.username
    const newpass = req.query.newpass
  
    pool.query(
      'UPDATE users SET password = $1 WHERE username = $2',
      [newpass, username],
      (error, results) => {
        if (error) {
          throw error
        }
        res.status(200).json({STATUS: 'Password updated successfully'})
      }
    )
}
  
const deleteUser = (req, res) => {
    const username = req.query.username
  
    pool.query('DELETE FROM users WHERE username = $1', [username], (error, results) => {
      if (error) {
        throw error
      }
      res.status(200).json({STATUS: 'Password updated successfully'})
    })
}

module.exports = {
    login,
    createUser,
    updateUser,
    deleteUser,
}
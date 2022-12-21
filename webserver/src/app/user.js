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
                    LOGIN: true,
                    message: 'You logged in successfully!'
                }
                res.send({STATUS})
            }
             else {
                const STATUS = {
                LOGIN: false,
                message: 'Password is incorrect.'
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
    const username = req.query.username
    const pass = req.query.pass


  pool.query(`SELECT EXISTS(SELECT 1 FROM users WHERE username='${username}')`, (error, results) => {
        if(results.rows[0].exists == false){
        pool.query('INSERT INTO users (username, password) VALUES ($1, $2)', [username, pass], (error, results) => {
            if (error) {
              res.send(error)
            }
            else{
              for(let i = 1; i <= 45; i++){
                pool.query(`INSERT INTO searched_tags (tag_id, username, count) VALUES (${i}, '${username}', ${getRandomInt(50)})`, (error, results) => {
                  if(error){
                    throw error
                  }
                })
              }
              const STATUS = {
                SIGNUP: true,
                message: 'Account successfully created'
            }
              res.json({STATUS})
            }
          })
        }
        else{
          const STATUS = {
            SIGNUP: false,
            message: 'Account with that username already exists'
        }
          res.json({STATUS})
        }})

  }
function getRandomInt(max) {
    return Math.floor(Math.random() * max);
}
  
const updateUser = (req, res) => {
    const username = req.query.username
    const oldpass = req.query.oldpass
    const newpass = req.query.newpass

    pool.query(`SELECT password FROM users WHERE username = '${username}'`, (error, results) => {
      if(results.rows[0].password == oldpass){
        pool.query(
          'UPDATE users SET password = $1 WHERE username = $2',
          [newpass, username],
          (error, results) => {
            if (error) {
              res.send(error)
            }
            else{
              const STATUS = {
                UPDATE: true,
                message: 'Password updated successfully!'
            }
              res.json({STATUS})
            }
          }
        )
      }
      else{
        const STATUS = {
          UPDATE: false,
          message: 'Old password is incorrect!'
      }
      res.send({STATUS})
      }
    })
  
}
  
const deleteUser = (req, res) => {
    const username = req.query.username
  
    pool.query('DELETE FROM users WHERE username = $1', [username], (error, results) => {
      if (error) {
        res.send(error)
        return
      }
      res.json({STATUS: 'Account deleted successfully'})
    })
}

module.exports = {
    login,
    createUser,
    updateUser,
    deleteUser,
}
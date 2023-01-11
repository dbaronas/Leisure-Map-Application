const Pool = require('pg').Pool
const pool = new Pool({
    user: 'admin',
    host: '193.219.91.103',
    port: 9170,
    database: 'pblDB',
    password: 'admin'
  })

  module.exports = pool

/*const Pool = require('pg').Pool
const pool = new Pool({
    user: 'admin',
    host: '10.0.0.160',
    database: 'pblDB',
    password: 'admin'
  })

  module.exports = pool*/
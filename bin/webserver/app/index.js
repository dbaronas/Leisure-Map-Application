const db = require('./databaseQueries')
const api = require('./APIs')
const score = require('./algorithm')
const overpass = require('./updateOverpass')
const user = require('./user')
const recom = require('./recommendation')

module.exports = {
    db,
    api,
    score,
    overpass,
    user,
    recom
}
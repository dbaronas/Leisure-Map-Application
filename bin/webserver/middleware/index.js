const limiter = require('./rateLimit')
const auth = require('./basic-auth')

module.exports = {
    limiter,
    auth
}
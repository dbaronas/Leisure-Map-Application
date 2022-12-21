const basicAuth = require('express-basic-auth')

const auth = basicAuth({
    users: { 'ArDoMa2002': 'PBLproject2022' },
    challenge: true,
    realm: 'Imb4T3st4pp',
})

module.exports = {
    auth,
}
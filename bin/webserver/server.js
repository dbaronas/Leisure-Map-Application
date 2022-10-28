const express = require('express')
const basicAuth = require('express-basic-auth')
const bodyParser = require('body-parser')
const app = express()
const db = require('./app/queries')
const api = require('./app/APIs')
const port = 3000

/*app.use(basicAuth({
    users: { 'ArDoMa2002': 'PBLproject2002' },
    challenge: true,
    realm: 'Imb4T3st4pp',
}))*/
app.use(bodyParser.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
)
app.get('/weather', api.getWeather)
app.get('/users', db.getUsers)
app.get('/users/:id', db.getUserById)
app.post('/users', db.createUser)
app.put('/users/:id', db.updateUser)
app.delete('/users/:id', db.deleteUser)

app.listen(port, () => {
    console.log(`App running on port ${port}.`)
})
const express = require('express')
const bodyParser = require('body-parser')
const app = express()
const db = require('./app/databaseQueries')
const api = require('./app/APIs')
const score = require('./app/algorithm')
const test = require('./test/testing')
const overpass = require('./app/updateOverpass')
const user = require('./app/user')
const limiter = require('./middleware/rateLimit')
const port = 3000

app.use(bodyParser.json())
app.use(
  bodyParser.urlencoded({
    extended: true,
  })
)

//Authentication
app.get('/user/login', limiter.limiter, user.login)
app.get('/user/create', limiter.limiter2, user.createUser)
app.get('/user/update', limiter.limiter2, user.updateUser)
app.get('/user/delete', user.deleteUser)

//Update Overpass API
app.get('/update/overpassapi/cities', overpass.updateCities)
app.get('/update/overpassapi/leisure', overpass.updateLeisure)
app.get('/update/overpassapi/restaurants', overpass.updateRestaurant)

//Test connections to APIs and database
app.get('/testconnections', test.testConnections)
app.get('/score', score.getScore)

//Get any location from Overpass API
app.get('/locations', api.getLocations)

//Vilnius events API
app.get('/events', api.getVilniusEvents)

//Meteo API to get weather forecast
app.get('/weather', api.getWeather)

//Database tables
app.get('/table', db.getTable)

app.listen(port, () => {
    console.log(`App running on port ${port}.`)
})
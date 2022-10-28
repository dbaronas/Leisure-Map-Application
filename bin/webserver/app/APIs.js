const request = require('request')

const getWeather = (req, res) => {
    const city = req.query.city
    request('https://api.meteo.lt/v1/places/' + city + '/forecasts/long-term', (error, response, data) => {
    if (!error && response.statusCode == 200) {
      res.send(data)
    }
    if(response.statusCode == 404){
        const error = {
            code: "404",
            message: "Not Found",
            
        }
        res.json({error: error})
    }
  })
}

const getVilniusEvents = (req, res) => {
    request('https://www.vilnius-events.lt/api/', (error, response, data) => {
    if (!error && response.statusCode == 200) {
      res.send(data)
    }
  })
}

module.exports = {
    getWeather,
    getVilniusEvents,
  }

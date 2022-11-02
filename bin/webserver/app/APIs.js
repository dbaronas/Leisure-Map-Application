const request = require('request')
const overpass = require('./overpass/export')
const nodefetch = require('node-fetch');
const queryString = require('querystring')

const getWeather = (req, res) => {
  const city = req.query.city
  const url = 'https://api.meteo.lt/v1/places/' + city + '/forecasts/long-term'
  const encoded = encodeURI(url)
  request(encoded, (error, response, data) => {
    if (!error && response.statusCode == 200) {
      var parsedData = JSON.parse(data)
      res.send(parsedData)
    }
    else{
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
      var parsedData = JSON.parse(data)
      res.send(parsedData)
    }
  })
}

const getLocations = (req, res) => {
	const key = req.query.key
	const value = req.query.value
  async function main() {
	const query = new overpass.OverpassQuery()
		.setFormat('json')
		.setTimeout(30)
		.addElement1({
			type: 'node',
			tags: [{
				key: key,
				value: value,
				not: false,
			}],
		})
		.addElement2({
			type: 'way',
			tags: [{
				key: key,
				value: value,
				not: false,
			}],
		})

	  const response = await query.fetch();
    res.send(response)
}
main();
}

module.exports = {
    getWeather,
    getVilniusEvents,
    getLocations,
  }

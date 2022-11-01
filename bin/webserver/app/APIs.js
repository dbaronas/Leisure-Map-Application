const request = require('request')
const overpass = require('./overpass/export')
const nodefetch = require('node-fetch');

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

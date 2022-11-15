const request = require('request')
const overpass = require('./overpass/export')
const LRU = require('lru-cache')
const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '193.219.91.103',
  port: 9170,
  database: 'pblDB',
  password: 'admin'
})

const options = {

max: 500,
maxSize: 5000,
sizeCalculation: (value, key) => {
  return 1
},

ttl: 1000 * 60 * 60,

allowState: false,
updateAgeOnGet: false,
updateAgeOnHas: false,

fetchMethod: async (key, staleValue, {options, signal}) => {}

}

const cache = new LRU(options)

const getWeather = async (req, res) => {
  const city = req.query.city
  const parsedCity = city.charAt(0).toUpperCase() + city.slice(1)
  const id = await pool.query(`SELECT id FROM cities WHERE name='${parsedCity}'`)
  const parsedId = parseInt(id.rows[0].id)
  let api
  
  if(cache.has(parsedId) == true){
    res.json(cache.get(parsedId))
    console.log('cache')
  }
  else{
    const url = 'https://api.meteo.lt/v1/places/' + city + '/forecasts/long-term'
    const encodedURL = encodeURI(url)
    await request(encodedURL, (error, response, data) => {
    if (!error && response.statusCode == 200) {
      api = JSON.parse(data)
      cache.set(parsedId, api)
      res.send(api)
      console.log('api')
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
}

const getVilniusEvents = async (req, res) => {
  let api
  if(cache.has(1) == true){
    res.json(cache.get(1))
    console.log('cache')
  }
  else{
    const url = 'https://www.vilnius-events.lt/api/'
    const encodedURL = encodeURI(url)
    await request(encodedURL, (error, response, data) => {
    if (!error && response.statusCode == 200) {
      api = JSON.parse(data)
      cache.set(1, api)
      res.send(api)
      console.log('api')
    }
    })
  }
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

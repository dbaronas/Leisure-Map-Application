//const Pool = require('pg').Pool
const request = require('request')
/*const pool = new Pool({
  user: 'admin',
  host: '10.0.0.14',
  database: 'pblDB',
  password: 'admin'
})*/

const testConnections = (req, res) => {
/*pool.on('connect', () => {
    res.send(`Connected to the DB: ${process.env.NODE_ENV}`);
})*/
request('https://www.vilnius-events.lt/api/', (error, response) => {
    if(response.statusCode == 200){
        console.log(`Vilnius Events API is working! Response code: ${response.statusCode} \n`)
    }
    else{
        console.log(`Vilnius Events API is not working! Response code: ${response.statusCode} \n`)
    }
})
request('https://api.meteo.lt/v1/places/vilnius/forecasts/long-term', (error, response) => {
    if(response.statusCode == 200){
        console.log(`Meteo API is working! Response code: ${response.statusCode} \n`)
    }
    else{
        console.log(`Meteo API is not working! Response code: ${response.statusCode} \n`)
    }
})
request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22shop%22=%22outdoor%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', (error, response) => {
    if(response.statusCode == 200){
        console.log(`Overpass API is working! Response code: ${response.statusCode}`)
    }
    else{
        console.log(`Overpass API is not working! Response code: ${response.statusCode}`)
    }
})
res.send('Check console for results')
}

module.exports = {
    testConnections,
}
const request = require('request')
const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '193.219.91.103',
  port: 9170,
  database: 'pblDB',
  password: 'admin'
})

const updateAPI = (req, res) => {

    pool.query('TRUNCATE TABLE cities', (error) => {
        if (error) {
            throw error
          }
          console.log('Cities table data cleared')
    })
    pool.query('TRUNCATE TABLE locations', (error) => {
      if (error) {
          throw error
        }
        console.log('Location table data cleared')
        console.log('Updating API data')
  })
    request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22city%22](area.searchArea);way[%22place%22=%22city%22](area.searchArea);relation[%22place%22=%22city%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', (error, response, data) => {
    if (!error) {
        var parsedData = JSON.parse(data)
        for(var i = 0; i < parsedData.elements.length; i++){
        pool.query('INSERT INTO cities (id, name, lat, lon) VALUES ($1, $2, $3, $4)', [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
          })
    }
    
  }})
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22town%22](area.searchArea);way[%22place%22=%22town%22](area.searchArea);relation[%22place%22=%22town%22](area.searchArea););out body;>;out skel qt;', (error, response, data) => {
    if (!error) {
        var parsedData = JSON.parse(data)
        for(var i = 0; i < parsedData.elements.length; i++){
        pool.query('INSERT INTO cities (id, name, lat, lon) VALUES ($1, $2, $3, $4)', [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
          })
        
    }
  }})
    request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22tourism%22=%22museum%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', (error, response, data) => {
    if (!error) {
        var parsedData = JSON.parse(data)
        for(var i = 0; i < parsedData.elements.length; i++){
        pool.query(`INSERT INTO locations (id, name, opening_hours, phone, website, city, lat, lon, type) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, 'museum')`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].opening_hours, parsedData.elements[i].tags.phone, parsedData.elements[i].tags.website, parsedData.elements[i].tags['addr:city'], parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
          })
        
    }
    console.log('Overpass API updated successfully!')
    res.send('Check console for results')
    
  }})

}

module.exports = {
    updateAPI,
}
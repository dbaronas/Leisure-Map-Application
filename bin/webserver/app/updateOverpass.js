const request = require('request')
const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '193.219.91.103',
  port: 9170,
  database: 'pblDB',
  password: 'admin'
})

const updateCities = async (req, res) => {
    request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22city%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
    parsedData = await JSON.parse(data)
    for(let i = 0; i < parsedData.elements.length - 1; i++){
        pool.query(`SELECT EXISTS(SELECT 1 FROM city WHERE id=${parsedData.elements[i].id})`, (error, response) => {
        if(response.rows[0].exists == false){
          pool.query(`INSERT INTO city (id, name, lat, lon, type) VALUES ($1, $2, $3, $4, 'city')`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
          })
        }
      })
    }
  })
    request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22town%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
      parsedData = await JSON.parse(data)
      for(let i = 0; i < parsedData.elements.length - 1; i++){
        pool.query(`SELECT EXISTS(SELECT 1 FROM city WHERE id=${parsedData.elements[i].id})`, (error, response) => {
          if(response.rows[0].exists == false){
            pool.query(`INSERT INTO city (id, name, lat, lon, type) VALUES ($1, $2, $3, $4, 'city')`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
              if (error) {
                throw error
              }
            })
          }
        })
      }
  
const STATUS = {
      UPDATE: `Overpass API updated successfully`
    }
    res.send({STATUS})
  })
}

const updateLeisure = (req, res) => {
  let parsedData
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22leisure%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
    parsedData = await JSON.parse(data)
    for(let i = 0; i < parsedData.elements.length - 1; i++){
      pool.query(`SELECT EXISTS(SELECT 1 FROM place WHERE id=${parsedData.elements[i].id})`, (error, response) => {
        if(response.rows[0].exists == false){
          pool.query(`INSERT INTO place (id, name, opening_hours, phone, website, city, lat, lon, type) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].tags.opening_hours, parsedData.elements[i].tags.phone, parsedData.elements[i].tags.website, parsedData.elements[i].tags['addr:city'], parsedData.elements[i].lat, parsedData.elements[i].lon, parsedData.elements[i].tags.leisure], (error, results) => {
            if (error) {
              throw error
            }
          })
        }
      })
    }
  })
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22tourism%22=%22museum%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
    parsedData = await JSON.parse(data)
    for(let i = 0; i < parsedData.elements.length - 1; i++){
      pool.query(`SELECT EXISTS(SELECT 1 FROM place WHERE id=${parsedData.elements[i].id})`, async (error, response) => {
        if(response.rows[0].exists == false){
          pool.query(`INSERT INTO place (id, name, opening_hours, phone, website, city, lat, lon, type) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].tags.opening_hours, parsedData.elements[i].tags.phone, parsedData.elements[i].tags.website, parsedData.elements[i].tags['addr:city'], parsedData.elements[i].lat, parsedData.elements[i].lon, parsedData.elements[i].tags.leisure], (error, results) => {
            if (error) {
              throw error
            }
          })
        }
      })
    }

const STATUS = {
    UPDATE: `Overpass API updated successfully`
  }
  res.send({STATUS})
  })
}

const updateRestaurant = (req, res) => {
  let parsedData
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22amenity%22=%22restaurant%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
    parsedData = await JSON.parse(data)
    for(let i = 0; i < parsedData.elements.length - 1; i++){
      pool.query(`SELECT EXISTS(SELECT 1 FROM place WHERE id=${parsedData.elements[i].id})`, (error, response) => {
        if(response.rows[0].exists == false){
          pool.query(`INSERT INTO place (id, name, opening_hours, phone, website, city, lat, lon, type) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].tags.opening_hours, parsedData.elements[i].tags.phone, parsedData.elements[i].tags.website, parsedData.elements[i].tags['addr:city'], parsedData.elements[i].lat, parsedData.elements[i].lon, parsedData.elements[i].tags.amenity], (error, results) => {
            if (error) {
              throw error
            }
          })
        }
      })
    }
  })
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22amenity%22=%22fast_food%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', async (error, response, data) => {
    parsedData = await JSON.parse(data)
    for(let i = 0; i < parsedData.elements.length - 1; i++){
      pool.query(`SELECT EXISTS(SELECT 1 FROM place WHERE id=${parsedData.elements[i].id})`, (error, response) => {
        if(response.rows[0].exists == false){
          pool.query(`INSERT INTO place (id, name, opening_hours, phone, website, city, lat, lon, type) VALUES ($1, $2, $3, $4, $5, $6, $7, $8, $9)`, [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].tags.opening_hours, parsedData.elements[i].tags.phone, parsedData.elements[i].tags.website, parsedData.elements[i].tags['addr:city'], parsedData.elements[i].lat, parsedData.elements[i].lon, parsedData.elements[i].tags.amenity], (error, results) => {
            if (error) {
              throw error
            }
          })
        }
      })
    }

const STATUS = {
    UPDATE: `Overpass API updated successfully`
  }
  res.send({STATUS})
  })
}

module.exports = {
    updateCities,
    updateLeisure,
    updateRestaurant,
}
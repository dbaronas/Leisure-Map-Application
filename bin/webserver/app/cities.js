const request = require('request')
const Pool = require('pg').Pool
const pool = new Pool({
  user: 'admin',
  host: '10.0.0.14',
  database: 'pblDB',
  password: 'admin'
})

const putCities = (req, res) => {

    request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22city%22](area.searchArea);way[%22place%22=%22city%22](area.searchArea);relation[%22place%22=%22city%22](area.searchArea););out%20body;%3E;out%20skel%20qt;', (error, response, data) => {
    if (!error) {
        var parsedData = JSON.parse(data)
        for(var i = 0; i < parsedData.elements.length; i++){
        /*pool.query('INSERT INTO cities (id, name, lat, lon) VALUES ($1, $2, $3, $4)', [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
            response.status(201).send(`City added`)
          })*/
        console.log('res: ', parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon)
    }
    
  }})
  request('https://overpass-api.de/api/interpreter?data=[out:json][timeout:25];area(id:3600072596)-%3E.searchArea;(node[%22place%22=%22town%22](area.searchArea);way[%22place%22=%22town%22](area.searchArea);relation[%22place%22=%22town%22](area.searchArea););out body;>;out skel qt;', (error, response, data) => {
    if (!error) {
        var parsedData = JSON.parse(data)
        for(var i = 0; i < parsedData.elements.length; i++){
        /*pool.query('INSERT INTO cities (id, name, lat, lon) VALUES ($1, $2, $3, $4)', [parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon], (error, results) => {
            if (error) {
              throw error
            }
            response.status(201).send(`City added`)
          })*/
        console.log('res: ', parsedData.elements[i].id, parsedData.elements[i].tags.name, parsedData.elements[i].lat, parsedData.elements[i].lon)
    }
    res.send('ended')
    
  }})
    
}

const getCity = (request, response) => {
  pool.query('SELECT * FROM cities ORDER BY id ASC', (error, results) => {
    if (error) {
      throw error
    }
const Project = {
    Project_name: "Maps.Relax",
    Team: "Kernel Technologies"
    
}
JSON.stringify();
    response.status(200).json({Project, Cities: results.rows})
  })
}

const createCity = (request, response) => {
  const { id, name, lat, lon } = request.body

  pool.query('INSERT INTO cities (id, name, lat, lon) VALUES ($1, $2, $3, $4)', [id, name, lat, lon], (error, results) => {
    if (error) {
      throw error
    }
    response.status(201).send(`City added`)
  })
}

const updateCity = (request, response) => {
    const id = parseInt(request.params.id)
    const { name, lat, lon } = request.body
  
    pool.query(
      'UPDATE cities SET name = $1, lat = $2, lon = $3 WHERE id = $4',
      [name, lat, lon, id],
      (error, results) => {
        if (error) {
          throw error
        }
        response.status(200).send(`User modified with ID: ${id}`)
      }
    )
  }
  

const deleteCity = (request, response) => {
  const id = parseInt(request.params.id)

  pool.query('DELETE FROM cities WHERE id = $1', [id], (error, results) => {
    if (error) {
      throw error
    }
    response.status(200).send(`User deleted with ID: ${id}`)
  })
}

module.exports = {
    getCity,
    createCity,
    deleteCity,
    putCities,
}
const overpass = require('./Overpass/export')

const getLocations = (req, res) => {
    var data
    async function main() {
	const query = new overpass.OverpassQuery()
		.setFormat('json')
		.setTimeout(30)
		.addElement1({
			type: 'node',
			tags: [{
				key: 'tourism',
				value: 'museum',
				not: false,
			}],
		})
		.addElement2({
			type: 'way',
			tags: [{
				key: 'tourism',
				value: 'museum',
				not: false,
			}],
		})
		.addElement3({
			type: 'relation',
			tags: [{
				key: 'tourism',
				value: 'museum',
				not: false,
			}],
		});

	const response = await query.fetch();
    res.send(response)
}
main();
}

module.exports = {
    getLocations,
}
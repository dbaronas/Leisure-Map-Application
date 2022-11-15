const getScore = (req, res) => {
    var distanceScore = parseFloat(req.query.distance)
    var reviewScore = parseFloat(req.query.reviewScore)
    var isPreffered = parseInt(req.query.isPreffered)
    var isOpen = parseInt(req.query.isOpen)

    if(50 / distanceScore > 50){
        distanceScore = 50
    }
    
    const score = (50 / distanceScore) + (reviewScore * 2) + (isPreffered * 10) + (isOpen * 30)
    
    res.json({Score: parseFloat(score.toFixed(2))})
}

module.exports = {
    getScore,
}
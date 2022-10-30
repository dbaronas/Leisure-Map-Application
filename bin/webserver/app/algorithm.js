const getScore = (req, res) => {
    var distanceScore = parseFloat(req.query.distance)
    var reviewScore = parseFloat(req.query.reviewScore)
    var isPreffered = parseInt(req.query.isPreffered)
    var isBusy = parseInt(req.query.isBusy)

    if(25 / distanceScore > 25){
        distanceScore = 25
    }
    
    const score = distanceScore + (reviewScore * 10) + (isPreffered * 10) + (Math.abs(isBusy - 1) * 10)
    
    res.json({Score: parseFloat(score.toFixed(2))})
}

module.exports = {
    getScore,
}
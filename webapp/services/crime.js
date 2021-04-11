export async function getData() {
  const apiFile = await require('~/static/crimeReport.json')
  return {
    currentLocation: {
      lat: apiFile.latitude,
      lon: apiFile.longitude,
    },
    events: apiFile.crimeSet,
    statistics: {
      locationRank: apiFile.score,
      events: {
        vehicleTheft: apiFile.vehicleTheft,
        arson: apiFile.arson,
        robbery: apiFile.robbery,
        violence: apiFile.violence,
      },
    },
  }
}

export async function getData() {
  const apiFile = await require('~/static/crimeReport.json')
  return apiFile
}

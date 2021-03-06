import clientAPI from './repository'

const baseURL = 'https://nominatim.openstreetmap.org'

export function currentCoordinates() {
  return new Promise((resolve, reject) => {
    navigator.geolocation.getCurrentPosition(
      ({ coords }) => resolve(coords),
      // Reject if the user doesn't
      // allow accessing their location.
      (error) => reject(error)
    )
  })
}

export async function addressByCoordinates({ latitude, longitude }) {
  const { data } = await clientAPI(baseURL).get('/reverse', {
    params: {
      format: 'jsonv2',
      lat: latitude,
      lon: longitude,
    },
  })

  return {
    display_name: data.display_name,
    address: data.address,
    coordinates: { lat: latitude, lon: longitude },
  }
}

export async function currentAddress() {
  const coordinates = await currentCoordinates()

  return addressByCoordinates(coordinates)
}

export async function searchAddress(query) {
  const { data } = await clientAPI(baseURL).get('/search', {
    params: {
      format: 'jsonv2',
      q: query,
    },
  })

  return data
}

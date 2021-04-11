<template>
  <div>
    <Navbar :initial-search="initialSearch" @search-address="searchAddress" />
    <div id="map-container">
      <LeafletMap
        :current-location="location"
        :events="events"
        :statistics="statistics"
      />
    </div>
  </div>
</template>

<script>
import * as crimeService from '~/services/crime'
import * as locationService from '~/services/location'
export default {
  data() {
    return {
      location: null,
      events: [],
      statistics: {},
      initialLocation: {}, // used for when starting the app
    }
  },
  computed: {
    initialSearch() {
      return this.initialLocation?.display_name || ''
    },
  },
  async created() {
    try {
      const {
        currentLocation,
        events,
        statistics,
      } = await crimeService.getData()
      this.location = currentLocation
      this.events = events
      this.statistics = statistics
      this.initialLocation = await locationService.addressByCoordinates({
        latitude: currentLocation.lat,
        longitude: currentLocation.lon,
      })
    } catch (error) {
      console.log(error)
    }
  },
  methods: {
    searchAddress($event) {
      this.location = $event
    },
  },
}
</script>

<style>
html,
body {
  margin: 0;
  padding: 0;
  height: 100%;
}
#map-container {
  position: absolute;
  top: 65px;
  overflow: hidden;
  bottom: 0;
  width: 100%;
}
</style>

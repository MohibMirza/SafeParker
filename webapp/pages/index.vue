<template>
  <div>
    <Navbar @search-address="searchAddress" />
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
export default {
  data() {
    return {
      location: null,
      events: [],
      statistics: {},
    }
  },
  async created() {
    const { currentLocation, events, statistics } = await crimeService.getData()
    this.location = currentLocation
    this.events = events
    this.statistics = statistics
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

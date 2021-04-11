<template>
  <div>
    <Navbar @search-address="searchAddress" />
    <div id="map-container">
      <LeafletMap :current-location="location" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      location: null,
    }
  },
  methods: {
    searchAddress($event) {
      console.log(('EVENT', $event))
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

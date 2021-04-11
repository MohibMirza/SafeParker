<template>
  <div>
    <Navbar />
    <div id="map-container">
      <LeafletMap />
    </div>
  </div>
</template>

<script>
export default {}
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

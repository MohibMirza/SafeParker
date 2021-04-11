<template>
  <div style="height: 100%; width: 100%">
    <l-map
      ref="map"
      :zoom="zoom"
      :center="position || center"
      :options="mapOptions"
      style="height: 95%"
      @dblclick="onMapClick"
      @update:center="centerUpdate"
      @update:zoom="zoomUpdate"
    >
      <l-tile-layer
        :url="tileProvider.url"
        :attribution="tileProvider.attribution"
      />
      <l-marker v-if="position" :lat-lng="position" />

      <!-- <l-marker :lat-lng="withPopup">
        <l-popup>
          <div>I am a popup</div>
        </l-popup>
      </l-marker> -->
      <!-- <l-marker v-for="(event, index) in events" :key="index" :lat-lng="event">
        <l-icon
          :icon-url="`/images/${event.icon}.png`"
          shadow-url="/images/marker-shadow.png"
        >
        </l-icon>
      </l-marker> -->
      <l-control v-if="statistics" position="topright" class="w-60">
        <div class="col-span-12">
          <div class="flex flex-row bg-white shadow-sm rounded p-4">
            <div
              class="flex items-center justify-center flex-shrink-0 h-12 w-12 rounded-xl text-4xl"
              :class="`bg-${rank.color}-100 text-${rank.color}-500`"
            >
              <i class="fas" :class="`fa-${rank.icon}`"></i>
            </div>
            <div class="flex flex-col flex-grow ml-4">
              <div class="text-sm text-gray-500">Safety Rank</div>
              <div class="font-bold text-lg" :class="`text-${rank.color}-500`">
                {{ rank.value }}
              </div>
            </div>
          </div>
        </div>
      </l-control>
      <l-control-zoom position="bottomright"></l-control-zoom>
    </l-map>
  </div>
</template>

<script>
import 'leaflet/dist/leaflet.css'
import 'leaflet-gesture-handling/dist/leaflet-gesture-handling.css'

import { latLng, Icon } from 'leaflet'

import { LMap, LTileLayer, LMarker, LControl, LControlZoom } from 'vue2-leaflet'

delete Icon.Default.prototype._getIconUrl
Icon.Default.mergeOptions({
  popupAnchor: [0, -35],
  iconRetinaUrl: require('leaflet/dist/images/marker-icon-2x.png'),
  iconUrl: require('leaflet/dist/images/marker-icon.png'),
  shadowUrl: require('leaflet/dist/images/marker-shadow.png'),
})

export default {
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LControl,
    LControlZoom,
  },
  props: {
    currentLocation: {
      type: Object,
      default() {
        return {}
      },
    },
    events: {
      type: Array,
      default() {
        return []
      },
    },
    statistics: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  data() {
    return {
      map: null,
      zoom: 4,
      center: latLng(39, -98), // USA
      mapOptions: {
        zoomSnap: 1,
        minZoom: 4,
        zoomControl: false,
      },
      position: null,

      tileProvider: {
        attribution:
          '&copy; <a href="http://osm.org/copyright" target="_blank">OpenStreetMap</a> contributors</a>',
        url: 'https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png',
      },
      currentZoom: 4,
      currentCenter: latLng(47.41322, -1.219482),
    }
  },
  computed: {
    rank() {
      const locationRank = this.statistics?.locationRank
      if (locationRank === 0) {
        return { color: 'green', value: 'A', icon: 'thermometer-empty' }
      } else if (locationRank === 1) {
        return { color: 'purple', value: 'B', icon: 'thermometer-quarter' }
      } else if (locationRank === 2) {
        return { color: 'yellow', value: 'C', icon: 'thermometer-half' }
      } else if (locationRank === 3) {
        return { color: 'red', value: 'D', icon: 'thermometer-three-quarters' }
      }
      return null
    },
  },
  watch: {
    currentLocation(location) {
      if (location && location.coordinates) {
        this.updateLocation(
          location.coordinates.lat,
          location.coordinates.lon,
          18
        )
      }
    },
    position: {
      deep: true,
      handler(value) {
        // API Call to retrieve surrounding data
        // this.address = await this.getAddress()
        this.$emit('input', { position: value })
      },
    },
  },
  created() {
    if (this.currentLocation) {
      this.updateLocation(this.currentLocation.lat, this.currentLocation.lon, 4)
    }
  },
  mounted() {
    this.map = this.$refs.map.mapObject
  },
  methods: {
    zoomUpdate(zoom) {
      this.currentZoom = zoom
    },
    centerUpdate(center) {
      this.currentCenter = center
    },
    onMapClick(value) {
      this.position = value.latlng
    },
    updateLocation(lat, lon, zoom) {
      this.center = latLng(lat, lon)
      this.position = this.center
      this.$nextTick(() => {
        this.map.setView(this.position, zoom)
      })
    },
  },
}
</script>
<style>
.leaflet-control-container .leaflet-top {
  z-index: 990;
}
</style>

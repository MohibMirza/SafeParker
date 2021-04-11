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

      <l-marker
        v-for="(event, index) in eventsIcon"
        :key="index"
        :lat-lng="eventLatLng(event)"
        :visible="showCrime"
      >
        <l-popup>
          <div>{{ event.category }}</div>
        </l-popup>
        <l-icon>
          <div>
            <i class="text-2xl" :class="event.icon"></i>
          </div>
        </l-icon>
      </l-marker>
      <l-control v-if="statistics" position="topright" class="w-60">
        <crime-statistics :statistics="statistics" />
      </l-control>
      <l-control position="bottomleft" class="w-60">
        <button
          type="button"
          class="flex justify-center p-2 m-1 text-white transition-colors duration-200 transform rounded-md bg-indigo-500 hover:bg-indigo-300 focus:outline-none focus:bg-indigo-300"
          @click="toggleCrime"
        >
          {{ showCrime ? 'Hide Crime' : 'Show Crime' }}
        </button>
        <button
          type="button"
          class="flex justify-center p-2 m-1 text-white transition-colors duration-200 transform rounded-md bg-indigo-500 hover:bg-indigo-300 focus:outline-none focus:bg-indigo-300"
          @click="resetCenter"
        >
          Reset Center
        </button>
      </l-control>
      <l-control-zoom position="bottomright"></l-control-zoom>
    </l-map>
  </div>
</template>

<script>
import 'leaflet/dist/leaflet.css'
import 'leaflet-gesture-handling/dist/leaflet-gesture-handling.css'

import { latLng, Icon } from 'leaflet'

import {
  LMap,
  LTileLayer,
  LMarker,
  LPopup,
  LIcon,
  LControl,
  LControlZoom,
} from 'vue2-leaflet'
import { crimeType } from '~/util/crime.util'

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
    LPopup,
    LIcon,
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

      showCrime: false,

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
    eventsIcon() {
      return this.events.map((event) => {
        return {
          icon: crimeType[event.category]?.icon || 'fas fa-exclamation',
          ...event,
        }
      }, [])
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
    resetCenter() {
      this.center = this.position
      this.$nextTick(() => {
        this.map.setView(this.position, 16)
      })
    },
    eventLatLng(event) {
      return latLng(event.latitude, event.longitude)
    },
    toggleCrime() {
      this.showCrime = !this.showCrime
    },
  },
}
</script>
<style>
.leaflet-control-container .leaflet-top {
  z-index: 990;
}
</style>

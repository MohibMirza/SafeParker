<template>
  <div>
    <location-frame @local-address="$emit('search-address', $event)">
      <div
        slot-scope="{ address, fetchAddress, geolocationSupported, error }"
        class="bg-transparent border rounded-md w-full focus-within:ring ring-primary focus-within:border-indigo-500 flex flex-wrap justify-between"
      >
        <button
          v-if="geolocationSupported && !error"
          type="button"
          class="bg-transparent pl-2"
          :class="{ 'text-blue-500': !!address }"
          @click="fetchAddress"
        >
          <i class="fas fa-location-arrow"></i>
        </button>
        <input
          v-model="searchTerm"
          type="search"
          class="flex-1 p-2 m-1 text-gray-700 placeholder-gray-400 bg-transparent border-none appearance-none focus:outline-none focus:placeholder-transparent focus:ring-0"
          :placeholder="address ? 'Marked Location' : 'Search'"
        />
        <button
          type="button"
          class="flex justify-center p-2 m-1 text-white transition-colors duration-200 transform rounded-md bg-indigo-500 hover:bg-indigo-300 focus:outline-none focus:bg-indigo-300"
          @click="searchAddress"
        >
          Go
        </button>
      </div>
    </location-frame>
  </div>
</template>
<script>
import LocationFrame from './LocationFrame'
export default {
  components: { LocationFrame },
  data() {
    return {
      searchTerm: '',
      localAddress: null,
    }
  },
  methods: {
    searchAddress() {
      // API Call to retrieve surrounding data
      if (this.searchTerm) {
        this.$emit('search-address', this.searchTerm)
      } else {
        alert('Address could not be found')
      }
    },
  },
}
</script>

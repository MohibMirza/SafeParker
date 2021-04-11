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
          :value="searchTerm"
          type="search"
          class="flex-1 p-2 m-1 text-gray-700 placeholder-gray-400 bg-transparent border-none appearance-none focus:outline-none focus:placeholder-transparent focus:ring-0"
          :placeholder="address ? 'Marked Location' : 'Search'"
          @input="updateSearchTerm"
        />
        <button
          type="button"
          class="flex justify-center p-2 m-1 text-white transition-colors duration-200 transform rounded-md bg-indigo-500 hover:bg-indigo-300 focus:outline-none focus:bg-indigo-300"
          @click="searchAddress"
        >
          Go
        </button>
        <div
          id="search-dropdown"
          class="absolute shadow top-full bg-white w-1/3 lef-0 rounded max-h-select overflow-y-auto svelte-5uyqqj"
        >
          <div class="flex flex-col w-full">
            <div
              v-for="result in searchResults"
              :key="result.place_id"
              class="cursor-pointer w-full border-gray-100 rounded-t border-b hover:bg-indigo-100"
              @click="setLocalResult(result)"
            >
              <div
                class="flex w-full items-center p-2 pl-2 border-transparent border-l-2 relative hover:border-indigo-100"
              >
                <div class="w-full items-center flex">
                  <div class="mx-2 leading-6">{{ result.display_name }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </location-frame>
  </div>
</template>
<script>
import debounce from 'lodash.debounce'
import LocationFrame from './LocationFrame'
import * as locationService from '~/services/location'

export default {
  components: { LocationFrame },
  data() {
    return {
      searchTerm: '',
      localAddress: null,
      searchResults: [],
    }
  },
  watch: {
    searchTerm() {
      this._debounceSearch()
    },
  },
  created() {
    this._debounceSearch = debounce(this.searchAddress, 200)
  },
  methods: {
    async searchAddress() {
      // API Call to geosearch
      if (this.searchTerm && !this.localAddress) {
        try {
          this.searchResults = await locationService.searchAddress(
            this.searchTerm
          )
        } catch (error) {
          console.log(error)
        }
      } else {
        this.searchResults = []
      }
    },
    setLocalResult(result) {
      this.localAddress = result
      this.$emit('search-address', {
        coordinates: {
          lat: this.localAddress.lat,
          lon: this.localAddress.lon,
        },
      })
      this.searchResults = []
      this.searchTerm = result.display_name
    },
    updateSearchTerm($event) {
      this.localAddress = null
      this.searchTerm = $event.target.value
    },
  },
}
</script>
<style scoped>
#search-dropdown {
  z-index: 1000;
}
</style>

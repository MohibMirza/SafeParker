import * as locationService from '~/services/location'

export default {
  data() {
    return {
      address: null,
      error: null,
      // Make it possible to conditionally render
      // elements based on if the geolocation API
      // is availabel or not.
      geolocationSupported: 'geolocation' in navigator,
      loading: false,
    }
  },
  methods: {
    async fetchAddress() {
      if (this.address) {
        return
      }
      try {
        this.setLoadingState()
        this.address = await locationService.currentAddress()
        this.$emit('local-address', this.address)
        // Reset the loading state after fetching the address.
        this.loading = false
      } catch (error) {
        this.setErrorState(error)
      }
    },
    setErrorState(error) {
      this.error = error
      this.loading = false
    },
    setLoadingState() {
      this.error = null
      this.loading = true
    },
  },
  render() {
    return this.$scopedSlots.default({
      // Data
      address: this.address,
      error: this.error,
      geolocationSupported: this.geolocationSupported,
      loading: this.loading,
      // Methods
      fetchAddress: this.fetchAddress,
    })
  },
}

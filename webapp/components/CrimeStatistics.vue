<template>
  <div v-if="statistics">
    <div v-if="rank" class="col-span-12">
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
    <div v-for="stat in eventStats" :key="stat.id" class="col-span-12">
      <div class="flex flex-row bg-white shadow-sm rounded p-4">
        <div
          class="flex items-center justify-center flex-shrink-0 h-6 w-12 rounded-xl text-xl"
          :class="`text-${rank.color}-500`"
        >
          {{ stat.value }}
        </div>
        <div class="flex flex-col flex-grow ml-4">
          <div class="text-sm text-gray-500">{{ stat.name }}</div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { crimeType } from '~/util/crime.util'

export default {
  props: {
    statistics: {
      type: Object,
      default() {
        return {}
      },
    },
  },
  computed: {
    rank() {
      const locationRank = this.statistics?.locationRank
      if (locationRank >= 8) {
        return { color: 'green', value: 'A', icon: 'thermometer-empty' }
      } else if (locationRank >= 6) {
        return { color: 'purple', value: 'B', icon: 'thermometer-quarter' }
      } else if (locationRank >= 3) {
        return { color: 'yellow', value: 'C', icon: 'thermometer-half' }
      } else if (locationRank >= 0) {
        return { color: 'red', value: 'D', icon: 'thermometer-three-quarters' }
      }
      return { color: 'gray', value: 'No Rank', icon: 'question' }
    },
    eventStats() {
      if (this.statistics.events) {
        return Object.entries(this.statistics.events).reduce((acc, event) => {
          acc.push({
            id: event[0],
            value: event[1],
            ...crimeType[event[0]],
          })
          return acc
        }, [])
      }
      return []
    },
  },
}
</script>

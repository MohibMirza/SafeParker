<template>
  <nav class="bg-white border-b border-gray-200 shadow-sm relative">
    <div class="max-w-7xl mx-auto px-2 sm:px-6 lg:px-8">
      <div class="relative flex items-center justify-between h-16">
        <div class="absolute inset-y-0 left-0 flex items-center sm:hidden">
          <!-- Mobile menu button-->
          <button
            type="button"
            class="inline-flex items-center justify-center p-2 rounded-md text-gray-400 hover:text-white hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-inset focus:ring-white"
            aria-controls="mobile-menu"
            aria-expanded="false"
            @click="isMenuOpen = !isMenuOpen"
          >
            <span class="sr-only">Open main menu</span>
            <!-- Icon when menu is closed. -->
            <svg
              class="h-6 w-6"
              :class="isMenuOpen ? 'hidden' : 'block'"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M4 6h16M4 12h16M4 18h16"
              />
            </svg>
            <!-- Icon when menu is open. -->
            <svg
              class="h-6 w-6"
              :class="isMenuOpen ? 'block' : 'hidden'"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
              aria-hidden="true"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>
        <div
          class="sm:flex-1 ml-10 mr-6 flex items-center justify-center sm:items-stretch sm:justify-start"
        >
          <navbar-logo />
          <div class="hidden sm:block sm:ml-6">
            <div class="flex space-x-4">
              <!-- Current: "bg-gray-900 text-white", Default: "text-gray-500 hover:bg-gray-700 hover:text-white" -->
              <a
                href="#"
                class="border-b border-gray-900 text-gray-800 py-2 text-sm font-medium"
                aria-current="page"
              >
                Map
              </a>

              <!-- <a
                href="#"
                class="text-gray-500 hover:bg-gray-700 hover:text-white px-3 py-2 rounded-md text-sm font-medium"
              >
                Dashboard
              </a> -->
            </div>
          </div>
        </div>
        <!-- <user-menu /> -->
        <search
          class="flex flex-1"
          :initial-search="initialSearch"
          @search-address="$emit('search-address', $event)"
        />
      </div>
    </div>

    <!-- Mobile menu -->
    <div v-if="isMenuOpen" id="mobile-menu" class="sm:hidden">
      <div class="px-2 pt-2 pb-3 space-y-1">
        <!-- Current: "bg-gray-900 text-white", Default: "text-gray-500 hover:bg-gray-700 hover:text-white" -->
        <a
          href="#"
          class="bg-gray-700 text-white block px-3 py-2 rounded-md text-base font-medium"
          aria-current="page"
        >
          Map
        </a>

        <!-- <a
          href="#"
          class="text-gray-500 hover:bg-gray-700 hover:text-white block px-3 py-2 rounded-md text-base font-medium"
        >
          Dashboard
        </a> -->
      </div>
    </div>
  </nav>
</template>
<script>
export default {
  props: {
    initialSearch: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      isMenuOpen: false,
    }
  },
}
</script>
<style>
nav {
  z-index: 999;
}
</style>

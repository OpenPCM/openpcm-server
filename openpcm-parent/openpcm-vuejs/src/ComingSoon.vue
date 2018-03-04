<template>
  <div class="col-xs-12 col-sm-8 col-sm-offset-2 col-md-6 col-md-offset-3">
    <img src="./assets/stethoscope.png">
    <h1>OpenPCM</h1>
    <h2>Open Patient Care Management</h2>
    <p>Coming soon...</p>
    <a v-bind:href="url">OpenPM GitHub</a>
  </div>
</template>

<script>
export default {
  data () {
    return {
	  url: 'https://github.com/gsugambit/openpcm/'
    }
  }
}
</script>

<style scoped>

</style>

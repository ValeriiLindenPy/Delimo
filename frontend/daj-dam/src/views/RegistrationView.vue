<template>
  <div class="container flex flex-col justify-center items-center mx-auto my-auto">
    <form class="flex flex-col p-6 gap-2 bg-st2 rounded-lg w-1/3 shadow-2xl" action="">
      <div class="flex gap-2 justify-center items-center">
        <b class="text-3xl font-extrabold text-st5">Daj</b>
        <i class="fa-solid fa-arrows-spin text-3xl text-st3"></i>
        <b class="text-3xl font-extrabold text-st5">Dam</b>
      </div>
      <label class="pt-3 text-xs">EMAIL:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="email" name="email" id="email" v-model="email" required/>

      <label class="pt-3 text-xs">LOZINKA:</label>
      <input class="border-b-2 border-st5 bg-st2 transition duration-500
      text-st5 focus:outline-none focus:ring-2
      focus:ring-st5" type="password" name="password"
             id="password" v-model="password" required/>

      <div class="flex items-center justify-center pt-2">
        <button type="submit" class="bg-st3 shadow-lg p-3 rounded-lg transition-colors duration-500 hover:bg-st4 hover:text-white" >ULOGUJTE SE</button>
      </div>
    </form>
  </div>
</template>

<script>
export default {
  name: "RegistrationView",
  data() {
    return {
      email: "",
      password: ""
    }
  }
}
</script>


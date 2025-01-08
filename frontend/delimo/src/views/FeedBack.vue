<template>
  <div class="flex justify-center mt-2 items-center md:container flex-col">
    <div class="flex flex-col w-full bg-st2 p-4 rounded-lg md:w-1/2">
      <h1 class="text-2xl font-bold mb-4">Pomoć i kontakt</h1>

      <p class="text-st5 mb-3">Ako ste pronašli grešku ili želite nešto da prijavite,
        kontaktirajte nas putem društvenih mreža,
        e-pošte ili preko kontakt forme ispod.</p>

      <div class="flex">
        <ul class="flex-col">
          <li class="flex gap-2 items-center mb-2">
            <i class="fa-regular fa-paper-plane text-st3"></i>
            <span>@delimoservice</span>
          </li>
          <li class="flex gap-2 items-center mb-2">
            <i class="fa-solid fa-at text-st3"></i>
            <span>delimoservice@gmail.com</span>
          </li>
          <li class="flex gap-2 items-center mb-2">
            <i class="fa-solid fa-phone text-st3"></i>
            <span>+381621110623</span>
          </li>
        </ul>
      </div>

      <div>
        <form @submit.prevent="sendFeedback">

          <!-- Ime -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="name">Ime</label>
            <input
                id="name"
                v-model="formData.name"
                class="border px-2 py-1 w-full rounded-lg"
                type="text"
                placeholder="Unesite vaše ime ili kako da vam se obratimo"
                required
            />
          </div>

          <!-- Naslov -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="name">Naslov</label>
            <input
                id="title"
                v-model="formData.title"
                class="border px-2 py-1 w-full rounded-lg"
                type="text"
                placeholder="Unesite naslov obraćanja"
                required
            />
          </div>


          <!-- Description -->
          <div>
            <label for="description" class="text-sm font-medium text-st5">Opis</label>
            <textarea
                id="description"
                v-model="formData.description"
                class="w-full mt-1 p-2 border rounded-md text-st5"
                placeholder="Unesite opis prijave"
                rows="4"
                required
            ></textarea>
          </div>



          <!-- Submit and Cancel buttons -->
          <div class="flex gap-2 items-center mb-2 justify-center mt-4">
            <button
                type="submit"
                class="bg-st4 mr-2 text-white py-2 px-4 w-full rounded hover:bg-st3 transition duration-500"
            >
              Pošalji
            </button>
          </div>

        </form>
      </div>

    </div>
  </div>
</template>

<script>


export default {
  name: "Feedback",

  data() {
    return {
      formData: {
        name: "",
        description: "",
        title: "",
      }
    };
  },
  created() {

  },
  methods: {
    sendFeedback() {
      alert(`Title: ${this.formData.title}`);
      this.$router.push(`/`);
    }
  }
};
</script>

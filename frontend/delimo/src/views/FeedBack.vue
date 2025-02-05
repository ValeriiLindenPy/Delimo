<template>
  <div class="flex justify-center mt-2 items-center md:container flex-col">
    <div class="flex flex-col w-full bg-st2 p-4 md:rounded-lg md:w-1/2">
      <h1 class="text-2xl font-bold mb-4">Pomoć i kontakt</h1>

      <p class="text-st5 mb-3">
        Ako ste pronašli grešku ili želite nešto da prijavite, kontaktirajte nas putem društvenih mreža, e-pošte ili preko kontakt forme ispod.
      </p>

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

          <!-- Email -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="subject">Email</label>
            <input
                id="email"
                v-model="formData.email"
                class="border px-2 py-1 w-full rounded-lg"
                type="email"
                placeholder="Unesite email obraćanja"
                required
            />
          </div>

          <!-- Naslov -->
          <div class="mb-4">
            <label class="block font-semibold mb-1" for="subject">Naslov</label>
            <input
                id="subject"
                v-model="formData.subject"
                class="border px-2 py-1 w-full rounded-lg"
                type="text"
                placeholder="Unesite naslov obraćanja"
                required
            />
          </div>

          <!-- Opis -->
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

          <!-- Submit Button -->
          <div class="flex gap-2 items-center mb-2 justify-center mt-4">
            <button
                type="submit"
                :disabled="isSubmitting"
                class="bg-st4 mr-2 text-white py-2 px-4 w-full rounded hover:bg-st3 transition duration-500"
            >
              {{ isSubmitting ? "Slanje..." : "Pošalji" }}
            </button>
          </div>

          <!-- Loader -->
          <Loader v-if="isSubmitting" />

          <!-- Success/Error Message -->
          <p v-if="message" :class="messageType === 'error' ? 'text-red-500' : 'text-green-500'">
            {{ message }}
          </p>

          <!-- PopUp Modal -->
          <PopUpModal :is-active="isModalOpen" @close="closeModal">
            <div class="flex flex-col items-center justify-center gap-2">
              <div class="bg-green-300 rounded-lg font-bold p-3">
                <p v-if="message" :class="messageType === 'error' ? 'text-red-500' : 'text-green-500'">
                  {{ message }}
                </p>
              </div>
              <button class="bg-st3 text-white font-medium py-2 px-4 rounded-md hover:bg-st4 transition"
                      @click="closeModal">
                Hvala!
              </button>
            </div>
          </PopUpModal>

        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { sendFeedback } from "@/services/emailService.js";
import Loader from "@/components/UI/Loader.vue";
import PopUpModal from "@/components/UI/PopUpModal.vue";

export default {
  name: "Feedback",
  components: {
    PopUpModal,
    Loader
  },
  data() {
    return {
      formData: {
        name: "",
        email: "",
        subject: "",
        description: ""
      },
      message: "",
      messageType: "",
      isSubmitting: false,
      isModalOpen: false // State to control modal visibility
    };
  },
  methods: {
    async sendFeedback() {
      if (!this.formData.name || !this.formData.subject || !this.formData.description) {
        this.message = "Sva polja su obavezna!";
        this.messageType = "error";
        return;
      }

      this.isSubmitting = true;
      this.message = "";

      try {
        const formData = new FormData();
        formData.append("name", this.formData.name);
        formData.append("subject", this.formData.subject);
        formData.append("email", this.formData.email);
        formData.append("description", this.formData.description);
        await sendFeedback(formData);

        this.message = "Uspešno poslato!";
        this.messageType = "success";
        this.formData = { name: "", subject: "", description: "", email: "" };

        this.isModalOpen = true;
      } catch (error) {
        this.message = "Došlo je do greške pri slanju!";
        this.messageType = "error";
      } finally {
        this.isSubmitting = false;
      }
    },
    closeModal() {
      this.isModalOpen = false;
    }
  }
};
</script>

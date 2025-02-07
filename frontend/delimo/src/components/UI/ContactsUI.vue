<template>
  <div class="flex p-2 flex-col text-center items-center justify-center bg-st2 md:rounded-lg">
    <div class="flex flex-col items-center justify-center">
      <button @click="showModal('phone')" class="p-2 bg-green-400 rounded-lg">Telefone {{ maskedTelephone }} Prikaži</button>
      <button v-if="viber" @click="showModal('viber')" class="p-2 mt-2 bg-viber rounded-lg text-white">Viber {{ maskedViber }} Prikaži</button>
    </div>


    <ModalContacts v-if="modalVisible" @close="modalVisible = false">
      <template #content>
        <div class="text-center p-4">
          <p v-if="selectedType === 'phone'">Telefon: {{ telephone }}</p>
          <p v-if="selectedType === 'viber'">Viber: {{ viber }}</p>
          <div class="mt-4">
            <a
                v-if="selectedType === 'phone'"
                :href="`tel:${telephone}`"
                class="p-2 bg-green-400 text-white rounded-lg"
            >Pozovi</a>
            <a
                v-if="selectedType === 'viber'"
                :href="`viber://chat/?number=%2B${toViber}&draft=${message}`"
                class="p-2 bg-viber text-white rounded-lg"
            >Otvori u Viberu</a>
          </div>
        </div>
      </template>
    </ModalContacts>
  </div>
</template>


<script>
import ModalContacts from "@/components/UI/ModalContacts.vue";

export default {
  name: 'ContactsUI',
  components: {
    ModalContacts,
  },
  data() {
    return {
      modalVisible: false,
      selectedType: null,
      message: '',
    };
  },
  created() {
    this.message = 'Zdravo! Da li mogu da rezerviram ' + this.name +
        ' iz Daj-Dam.rs. Gde i kada Vam odgovara?'
  },
  props: {
    telephone: {
      type: String,
      required: true,
    },
    viber: {
      type: String,
      required: false,
    },
    name: {
      type: String,
      required: true,
    }
  },
  methods: {
    showModal(type) {
      this.selectedType = type;
      this.modalVisible = true;
    },
  },
  computed: {
    maskedTelephone() {
      return this.telephone.slice(0, -4) + '****';
    },
    maskedViber() {
      return this.viber.slice(0, -4) + '****';
    },
    toViber() {
      const viberLink = this.viber.replace('+', '');
      console.log(viberLink);
      return viberLink;
    }
  }
}
</script>
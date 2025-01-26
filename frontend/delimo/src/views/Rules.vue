<template>
  <div class="container mx-auto px-4 py-6">
    <!-- Header Section -->
    <div class="bg-gradient-to-r from-indigo-500 to-purple-500 text-white rounded-lg p-6 shadow-lg">
      <h1 class="font-bold text-3xl text-center">Frequently Asked Questions (F.A.Q.)</h1>
      <p class="text-center mt-2">Sve što treba da znate o platformi Delimo</p>
    </div>

    <!-- FAQ Content -->
    <div class="mt-6">
      <!-- Navigation Tabs -->
      <div class="flex justify-center space-x-4 mb-6">
        <button
            class="px-4 py-2 rounded-md bg-indigo-500 text-white font-bold hover:bg-indigo-600 focus:ring focus:ring-indigo-300"
            @click="scrollToSection('about')"
        >
          O nama
        </button>
        <button
            class="px-4 py-2 rounded-md bg-indigo-500 text-white font-bold hover:bg-indigo-600 focus:ring focus:ring-indigo-300"
            @click="scrollToSection('how-it-works')"
        >
          Kako funkcioniše
        </button>
        <button
            class="px-4 py-2 rounded-md bg-indigo-500 text-white font-bold hover:bg-indigo-600 focus:ring focus:ring-indigo-300"
            @click="scrollToSection('contact')"
        >
          Kontakt
        </button>
      </div>

      <!-- Sections -->
      <section id="about" class="bg-white rounded-lg p-6 shadow-md mb-6">
        <h2 class="font-bold text-2xl mb-3">O nama</h2>
        <div class="relative">
          <img
              class="float-right w-1/2 md:w-1/3 h-auto m-3 rounded-lg shadow-xl  dark:shadow-gray-800"
              src="/images/intro.gif"
              alt="intro"
          />
          <p class="text-gray-700 leading-relaxed mb-2">
            Delimo je platforma stvorena za povezivanje korisnika koji žele „pozajmiti” neku stvar za kratkoročnu ličnu upotrebu i onih koji su spremni da te stvari ustupe uz naknadu ili besplatno.
          </p>
          <p class="text-gray-700 leading-relaxed mb-2">
            Naš projekat nudi alternativu uobičajenoj kupovini, stavljajući akcenat na razmenu stvari. Ovo je posebno korisno za predmete koji su potrebni samo na kratko vreme. Ovakav pristup doprinosi svesnoj potrošnji, produžava životni ciklus stvari i pomaže u smanjenju broja nepotrebnih predmeta koji bi mogli završiti na deponijama.
          </p>
          <p class="text-gray-700 leading-relaxed mb-2">
            Misija Delimo jeste podrška ekološkoj ravnoteži i pažljivom odnosu prema prirodnim resursima. Verujemo da je razmena stvari umesto njihovog nekontrolisanog gomilanja važan korak ka odgovornom odnosu prema životnoj sredini.
          </p>
          <p class="text-gray-700 leading-relaxed mb-2">
            Delimo je neprofitna inicijativa koja ima za cilj stvaranje praktične i dostupne platforme za kratkoročno korišćenje stvari. Postavljanje oglasa na sajtu je besplatno, čime ističemo našu posvećenost principima društveno odgovorne potrošnje i jednakog pristupa stvarima za sve korisnike.
          </p>
          <p class="font-bold text-st3 mt-3">
            Pridružite se Delimo i delite stvari svesno!
          </p>
        </div>
      </section>


      <section id="how-it-works" class="bg-white rounded-lg p-6 shadow-md mb-6">
        <h2 class="font-bold text-2xl mb-3">Kako funkcioniše</h2>
        <ul class="list-decimal list-inside text-gray-700 leading-relaxed">
          <li class="font-bold">Registrujte se na platformu Delimo.</li>
          <li class="font-bold">Dodajte ili pronađite oglase za predmete koje želite razmeniti.</li>
          <div class="pl-4">
            <p class="text-gray-700 leading-relaxed mt-3">
              Na Delimo postoje 2 tipa oglasa: <b>Zahtev</b> ili <b>Stvar</b>.</p>
            <ul class="list-disc pl-6">
              <li>Da biste kreirali oglas, kliknite na dugme u meniju <b>„Kreiraj oglas“</b>.</li>
              <img class="m-2 shadow-xl  dark:shadow-gray-800" src="/images/additem.png" />
              <li>Zatim će vam biti ponuđena dva tipa oglasa.</li>
              <img class="m-2 shadow-xl  dark:shadow-gray-800" src="/images/oglastype.png" />
            </ul>



            <p class="text-gray-700 leading-relaxed mt-6">
              <b>Dodajte stvar </b>: Ako želite omogućiti korisnicima da stupe u kontakt s vama radi iznajmljivanja vašoj stvari, kliknite na <b>„Dodaj stvar“</b>.
            </p>
            <p> Nakon toga će se otvoriti meni za dodavanje predmeta. Tamo ćete trebati pružiti informacije o predmetu:</p>
            <ul class="list-disc pl-10">
              <li><b>Naziv</b> – naziv stvari</li>
              <li><b>Opis</b> –  kratak opis stvari</li>
              <li><b>Maksimalni period</b> –  maksimalni broj dana na koji biste mogli pozajmiti stvar</li>
              <li><b>Cena</b> –  cena po kojoj biste mogli pozajmiti predmet, možete navesti i besplatno. Po dogovoru, cena može služiti samo kao depozit.</li>
              <li>
                Zatim ćete trebati popuniti svoje kontakt informacije kako bi korisnici mogli stupiti u kontakt s vama. Ako ste već popunili kontakt podatke prilikom registracije, oni će automatski biti prikazani i neće biti potrebno dodatno popunjavanje.
              </li>
              <li>
                Takođe, možete dodati do 5 fotografija svom oglasu.
              </li>
            </ul>
          </div>


          <li class="font-bold mt-2">Stupite u kontakt sa korisnicima i dogovorite razmenu.</li>
        </ul>
<!--        <img-->
<!--            src="/images/how-it-works.svg"-->
<!--            alt="How it works illustration"-->
<!--            class="mt-4 rounded-lg shadow-md mx-auto w-3/4"-->
<!--        />-->
      </section>

      <section id="contact" class="bg-white rounded-lg p-6 shadow-md mb-6">
        <h2 class="font-bold text-2xl mb-3">Kontakt</h2>
        <p class="text-gray-700 leading-relaxed">
          Ako imate dodatnih pitanja, slobodno nas kontaktirajte putem emaila: <a href="mailto:kontakt@delimo.com" class="text-indigo-500 font-bold">kontakt@delimo.com</a>
        </p>
        <p class="text-gray-700 leading-relaxed mt-3">
          Ili nas pratite na društvenim mrežama za najnovije informacije i savete.
        </p>
        <div class="flex space-x-4 mt-4">
          <a href="#" class="text-indigo-500 hover:text-indigo-700">
            <i class="fab fa-facebook fa-2x"></i>
          </a>
          <a href="#" class="text-indigo-500 hover:text-indigo-700">
            <i class="fab fa-instagram fa-2x"></i>
          </a>
          <a href="#" class="text-indigo-500 hover:text-indigo-700">
            <i class="fab fa-twitter fa-2x"></i>
          </a>
        </div>
      </section>
    </div>
  </div>
</template>

<script>
export default {
  methods: {
    scrollToSection(id) {
      const section = document.getElementById(id);
      if (section) {
        section.scrollIntoView({ behavior: 'smooth' });
      }
    },
  },
};
</script>

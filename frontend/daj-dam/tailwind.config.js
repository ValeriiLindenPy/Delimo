/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        st0: "#aa91c4",
        st1: "#F6E9D8",
        st2: "#E8DFF5",
        st3: "#F38B4A",
        st4: "#4B306A",
        st5: "#342348",
        st6: "#F3C8A5",
        st7: "#6B5C5C",
        st9: "#daceb3"
      },
      fontFamily: {
        Inter: ["Inter", "serif"]
      }
    },
    container: {
      padding: "2rem",
      center: true,
    },
    screen: {
      sm: "640px",
      md: "768px",
    }
  },
  plugins: [],
}


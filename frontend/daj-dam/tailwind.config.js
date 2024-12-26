/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{vue,js,jsx,ts,tsx}"],
  theme: {
    extend: {
      colors: {
        vanilla: "#EAE2B7",
        orange: "#F77F00",
        fire: "#D62828",
        prussian: "#003049",
        xan: "#FCBF49",
        sea: "#2EC4B6",
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


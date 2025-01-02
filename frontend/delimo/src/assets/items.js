import {users} from "@/assets/users.js";
import {cities} from "@/assets/cities.js";

export const items = [
    {
        id: 1,
        name: "Električna bušilica Stayer",
        description: "Električna bušilica sa ergonomskim dizajnom — idealna za bušenje drveta, metala i drugih materijala. Pogodna za kućne poslove i lakše profesionalne zadatke. Radi na struju, opremljena je podesivom brzinom okretanja i funkcijom reverza.",
        available: false,
        maxPeriodDays: 3,
        image: [
            "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
            "https://i.ibb.co/DL5VbSY/27504-750x0.jpg",
            "https://i.ibb.co/zHNgfhB/44819-750x0.jpg",
        ],
        pricePerDay: 100,
        owner: {
            id: 1,
            city: cities.find(c => c.id === users.find(user => user.id === 1).cityID).name,
            address: users.find(user => user.id === 1).address,
            telephone: users.find(user => user.id === 1).telephone,
            viber: users.find(user => user.id === 1).viber,
        }
    },
    {
        id: 2,
        name: "Set za roštilj",
        description: "Kompletan set za roštilj sa svim potrebnim priborom za pripremu ukusnih obroka na otvorenom. Uključuje rešetku, hvataljke, četku za čišćenje i zaštitne rukavice.",
        available: true,
        maxPeriodDays: 7,
        image: [
            "https://i.ibb.co/album/roštilj-set1.jpg",
            "https://i.ibb.co/album/roštilj-set2.jpg",
        ],
        pricePerDay: null,
        owner: {
            id: 2,
            city: cities.find(c => c.id === users.find(user => user.id === 2).cityID).name,
            address: users.find(user => user.id === 2).address,
            telephone: users.find(user => user.id === 2).telephone,
            viber: users.find(user => user.id === 2).viber,
        }
    },
    {
        id: 3,
        name: "Bicikl za planinarenje",
        description: "Robustan planinski bicikl sa 21 brzinom, idealan za off-road avanture i vožnju po neravnom terenu. Opremljen amortizerima i disk kočnicama za sigurnu vožnju.",
        available: true,
        maxPeriodDays: 1,
        image: [
            "https://i.ibb.co/album/bicikl1.jpg",
            "https://i.ibb.co/album/bicikl2.jpg",
            "https://i.ibb.co/album/bicikl3.jpg",
        ],
        pricePerDay: 200,
        owner: {
            id: 3,
            city: cities.find(c => c.id === users.find(user => user.id === 3).cityID).name,
            address: users.find(user => user.id === 3).address,
            telephone: users.find(user => user.id === 3).telephone,
            viber: users.find(user => user.id === 3).viber,
        }
    },
    {
        id: 4,
        name: "Šator za kampovanje",
        description: "Prostran šator za 4 osobe, otporan na vodu i vetar. Idealan za porodična kampovanja i avanture u prirodi. Lako se postavlja i sklapa.",
        available: false,
        maxPeriodDays: 5,
        image: [
            "https://i.ibb.co/album/šator1.jpg",
            "https://i.ibb.co/album/šator2.jpg",
        ],
        pricePerDay: null,
        owner: {
            id: 4,
            city: cities.find(c => c.id === users.find(user => user.id === 4).cityID).name,
            address: users.find(user => user.id === 4).address,
            telephone: users.find(user => user.id === 4).telephone,
            viber: users.find(user => user.id === 4).viber,
        }
    },
    {
        id: 5,
        name: "Digitalni fotoaparat Canon",
        description: "Profesionalni digitalni fotoaparat sa 24.1 MP senzorom i mogućnošću snimanja Full HD videa. Uključuje objektiv od 18-55mm i torbu za nošenje.",
        available: true,
        maxPeriodDays: 2,
        image: [
            "https://i.ibb.co/album/fotoaparat1.jpg",
            "https://i.ibb.co/album/fotoaparat2.jpg",
            "https://i.ibb.co/album/fotoaparat3.jpg",
        ],
        pricePerDay: 500,
        owner: {
            id: 5,
            city: cities.find(c => c.id === users.find(user => user.id === 5).cityID).name,
            address: users.find(user => user.id === 5).address,
            telephone: users.find(user => user.id === 5).telephone,
            viber: users.find(user => user.id === 5).viber,
        }
    },
    {
        id: 6,
        name: "Električni trotinet",
        description: "Sklopivi električni trotinet sa maksimalnom brzinom od 25 km/h i dometom do 20 km. Idealan za gradsku vožnju i izbegavanje saobraćajnih gužvi.",
        available: true,
        maxPeriodDays: 3,
        image: [
            "https://i.ibb.co/album/trotinet1.jpg",
            "https://i.ibb.co/album/trotinet2.jpg",
        ],
        pricePerDay: 300,
        owner: {
            id: 6,
            city: cities.find(c => c.id === users.find(user => user.id === 6).cityID).name,
            address: users.find(user => user.id === 6).address,
            telephone: users.find(user => user.id === 6).telephone,
            viber: users.find(user => user.id === 6).viber,
        }
    },
    {
        id: 7,
        name: "Električni trotinet Xiaomi",
        description: "Sklopivi električni trotinet sa maksimalnom brzinom od 25 km/h i dometom do 30 km. Opremljen snažnim motorom i dugotrajnom baterijom.",
        available: true,
        maxPeriodDays: 7,
        image: [
            "https://i.ibb.co/album/Xiaomi-Trotinet1.jpg",
            "https://i.ibb.co/album/Xiaomi-Trotinet2.jpg",
        ],
        pricePerDay: 350,
        owner: {
            id: 7,
            city: cities.find(c => c.id === users.find(user => user.id === 7).cityID).name,
            address: users.find(user => user.id === 7).address,
            telephone: users.find(user => user.id === 7).telephone,
            viber: users.find(user => user.id === 7).viber,
        }
    },
    {
        id: 8,
        name: "Dron DJI Mavic Air",
        description: "Kompaktni dron sa 4K kamerom i naprednim funkcijama za stabilizaciju leta. Idealan za snimanje iz vazduha i kreiranje profesionalnih video zapisa.",
        available: false,
        maxPeriodDays: 3,
        image: [
            "https://i.ibb.co/album/DJI-Mavic-Air1.jpg",
            "https://i.ibb.co/album/DJI-Mavic-Air2.jpg",
            "https://i.ibb.co/album/DJI-Mavic-Air3.jpg",
        ],
        pricePerDay: 600,
        owner: {
            id: 8,
            city: cities.find(c => c.id === users.find(user => user.id === 8).cityID).name,
            address: users.find(user => user.id === 8).address,
            telephone: users.find(user => user.id === 8).telephone,
            viber: users.find(user => user.id === 8).viber,
        }
    },
    {
        id: 9,
        name: "PlayStation 5 konzola",
        description: "Najnovija generacija Sony konzole sa podrškom za 4K gaming. Uključuje dva kontrolera i nekoliko popularnih igara.",
        available: true,
        maxPeriodDays: 5,
        image: [
            "https://i.ibb.co/album/PS5-1.jpg",
            "https://i.ibb.co/album/PS5-2.jpg",
        ],
        pricePerDay: null,
        owner: {
            id: 9,
            city: cities.find(c => c.id === users.find(user => user.id === 9).cityID).name,
            address: users.find(user => user.id === 9).address,
            telephone: users.find(user => user.id === 9).telephone,
            viber: users.find(user => user.id === 9).viber,
        }
    },
    {
        id: 10,
        name: "Profesionalni mikser za DJ-eve",
        description: "Visokokvalitetni DJ mikser sa više kanala, efektima i USB podrškom. Savršen za nastupe uživo i miksovanje muzike.",
        available: false,
        maxPeriodDays: 5,
        image: [
            "https://i.ibb.co/album/DJ-Mikser1.jpg",
            "https://i.ibb.co/album/DJ-Mikser2.jpg",
            "https://i.ibb.co/album/DJ-Mikser3.jpg",
        ],
        pricePerDay: 700,
        owner: {
            id: 10,
            city: cities.find(c => c.id === users.find(user => user.id === 10).cityID).name,
            address: users.find(user => user.id === 10).address,
            telephone: users.find(user => user.id === 10).telephone,
            viber: users.find(user => user.id === 10).viber,
        }
    },
    {
        id: 11,
        name: "Karaoke set sa mikrofonom",
        description: "Kompletan karaoke set sa bežičnim mikrofonom i ugrađenim zvučnikom. Idealan za zabave i proslave.",
        available: true,
        maxPeriodDays: 2,
        image: [
            "https://i.ibb.co/album/Karaoke-Set1.jpg",
            "https://i.ibb.co/album/Karaoke-Set2.jpg",
        ],
        pricePerDay: 250,
        owner: {
            id: 1,
            city: cities.find(c => c.id === users.find(user => user.id === 1).cityID).name,
            address: users.find(user => user.id === 1).address,
            telephone: users.find(user => user.id === 1).telephone,
            viber: users.find(user => user.id === 1).viber,
        }
    },
    {
        id: 12,
        name: "Projektor za kućno kino",
        description: "HD projektor sa visokim kontrastom i svetlinom. Pogodan za gledanje filmova i prezentacije.",
        available: false,
        maxPeriodDays: 4,
        image: [
            "https://i.ibb.co/album/Projektor1.jpg",
            "https://i.ibb.co/album/Projektor2.jpg",
            "https://i.ibb.co/album/Projektor3.jpg",
        ],
        pricePerDay: 500,
        owner: {
            id: 2,
            city: cities.find(c => c.id === users.find(user => user.id === 2).cityID).name,
            address: users.find(user => user.id === 2).address,
            telephone: users.find(user => user.id === 2).telephone,
            viber: users.find(user => user.id === 2).viber,
        }
    },
    {
        id: 13,
        name: "Električna gitara Fender Stratocaster",
        description: "Kultna električna gitara sa prepoznatljivim zvukom. Idealna za rock, blues i jazz muziku.",
        available: true,
        maxPeriodDays: 6,
        image: [
            "https://i.ibb.co/album/Fender-Stratocaster1.jpg",
            "https://i.ibb.co/album/Fender-Stratocaster2.jpg",
        ],
        pricePerDay: null,
        owner: {
            id: 3,
            city: cities.find(c => c.id === users.find(user => user.id === 3).cityID).name,
            address: users.find(user => user.id === 3).address,
            telephone: users.find(user => user.id === 3).telephone,
            viber: users.find(user => user.id === 3).viber,
        }
    },
    {
        id: 14,
        name: "Šivaća mašina Brother",
        description: "Kompaktna šivaća mašina sa više funkcija. Pogodna za kućnu upotrebu i manje krojačke projekte.",
        available: false,
        maxPeriodDays: 5,
        image: [
            "https://i.ibb.co/album/Sivaca-Masina1.jpg",
            "https://i.ibb.co/album/Sivaca-Masina2.jpg",
        ],
        pricePerDay: 300,
        owner: {
            id: 4,
            city: cities.find(c => c.id === users.find(user => user.id === 4).cityID).name,
            address: users.find(user => user.id === 4).address,
            telephone: users.find(user => user.id === 4).telephone,
            viber: users.find(user => user.id === 4).viber,
        }
    },
    {
        id: 15,
        name: "Kompresor za vazduh",
        description: "Prenosivi kompresor za vazduh sa rezervoarom od 50 litara. Idealan za pneumatske alate i pumpanje guma.",
        available: true,
        maxPeriodDays: 20,
        image: [
            "https://i.ibb.co/album/Kompresor1.jpg",
            "https://i.ibb.co/album/Kompresor2.jpg",
        ],
        pricePerDay: 350,
        owner: {
            id: 5,
            city: cities.find(c => c.id === users.find(user => user.id === 5).cityID).name,
            address: users.find(user => user.id === 5).address,
            telephone: users.find(user => user.id === 5).telephone,
            viber: users.find(user => user.id === 5).viber,
        }
    },
    {
        id: 16,
        name: "Set za pecanje",
        description: "Kompletan set za pecanje koji uključuje štap, mašinicu, udice i dodatni pribor. Pogodan za početnike i iskusne ribolovce.",
        available: false,
        maxPeriodDays: 7,
        image: [
            "https://i.ibb.co/album/Pecaroski-Set1.jpg",
            "https://i.ibb.co/album/Pecaroski-Set2.jpg",
        ],
        pricePerDay: 150,
        owner: {
            id: 6,
            city: cities.find(c => c.id === users.find(user => user.id === 6).cityID).name,
            address: users.find(user => user.id === 6).address,
            telephone: users.find(user => user.id === 6).telephone,
            viber: users.find(user => user.id === 6).viber,
        }
    },
    {
        id: 17,
        name: "Krovni nosač za automobil",
        description: "Univerzalni krovni nosač pogodan za većinu automobila. Idealan za prevoz prtljaga, bicikala ili skija.",
        available: true,
        maxPeriodDays: 10,
        image: [
            "https://i.ibb.co/album/Krovni-Nosac1.jpg",
            "https://i.ibb.co/album/Krovni-Nosac2.jpg",
        ],
        pricePerDay: 200,
        owner: {
            id: 7,
            city: cities.find(c => c.id === users.find(user => user.id === 7).cityID).name,
            address: users.find(user => user.id === 7).address,
            telephone: users.find(user => user.id === 7).telephone,
            viber: users.find(user => user.id === 7).viber,
        }
    },
    {
        id: 18,
        name: "Električni roštilj Tefal",
        description: "Kompaktni električni roštilj sa neprijanjajućom pločom. Idealan za pripremu hrane u zatvorenom prostoru.",
        available: false,
        maxPeriodDays: 3,
        image: [
            "https://i.ibb.co/album/Elektricni-Rostilj1.jpg",
            "https://i.ibb.co/album/Elektricni-Rostilj2.jpg",
        ],
        pricePerDay: 250,
        owner: {
            id: 8,
            city: cities.find(c => c.id === users.find(user => user.id === 8).cityID).name,
            address: users.find(user => user.id === 8).address,
            telephone: users.find(user => user.id === 8).telephone,
            viber: users.find(user => user.id === 8).viber,
        }
    },
    {
        id: 19,
        name: "Set za kampovanje",
        description: "Kompletan set za kampovanje koji uključuje šator za 4 osobe, vreće za spavanje i podlogu. Idealan za porodične avanture u prirodi.",
        available: true,
        maxPeriodDays: 7,
        image: [
            "https://i.ibb.co/album/Kamp-Set1.jpg",
            "https://i.ibb.co/album/Kamp-Set2.jpg",
        ],
        pricePerDay: 400,
        owner: {
            id: 9,
            city: cities.find(c => c.id === users.find(user => user.id === 9).cityID).name,
            address: users.find(user => user.id === 9).address,
            telephone: users.find(user => user.id === 9).telephone,
            viber: users.find(user => user.id === 9).viber,
        }
    },
    {
        id: 20,
        name: "Profesionalni fotoaparat Nikon D850",
        description: "Visokokvalitetni DSLR fotoaparat sa 45.7 MP senzorom. Idealan za profesionalne fotografe i snimanje u visokoj rezoluciji.",
        available: false,
        maxPeriodDays: 5,
        image: [
            "https://i.ibb.co/album/Nikon-D850-1.jpg",
            "https://i.ibb.co/album/Nikon-D850-2.jpg",
            "https://i.ibb.co/album/Nikon-D850-3.jpg",
        ],
        pricePerDay: 1000,
        owner: {
            id: 10,
            city: cities.find(c => c.id === users.find(user => user.id === 10).cityID).name,
            address: users.find(user => user.id === 10).address,
            telephone: users.find(user => user.id === 10).telephone,
            viber: users.find(user => user.id === 10).viber,
        }
    }
];
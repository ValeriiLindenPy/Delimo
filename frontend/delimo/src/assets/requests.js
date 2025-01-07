import { users } from "@/assets/users.js";
import { cities } from "@/assets/cities.js";

export const requests = [
    {
        id: 1,
        name: "Električna bušilica Stayer",
        maxPeriodDays: 3,
        pricePerDay: null,
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
        maxPeriodDays: 7,
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
        maxPeriodDays: 1,
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
        maxPeriodDays: 5,
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
        maxPeriodDays: 2,
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
        maxPeriodDays: 3,
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
        maxPeriodDays: 7,
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
        maxPeriodDays: 3,
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
        maxPeriodDays: 5,
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
        maxPeriodDays: 5,
        pricePerDay: 700,
        owner: {
            id: 10,
            city: cities.find(c => c.id === users.find(user => user.id === 10).cityID).name,
            address: users.find(user => user.id === 10).address,
            telephone: users.find(user => user.id === 10).telephone,
            viber: users.find(user => user.id === 10).viber,
        }
    }

];

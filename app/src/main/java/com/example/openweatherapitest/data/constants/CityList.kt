package com.example.openweatherapitest.data.constants

import com.example.openweatherapitest.data.City

class CityList {
    companion object {
        val data = listOf(
            City(
                name = "Rennes",
                lat = "48.1119800",
                long = "-1.6742900",
            ),
            City(
                name = "Paris",
                lat = "48.8534100",
                long = "2.3488000",
            ),
            City(
                name = "Nantes",
                lat = "47.2172500",
                long = "-1.5533600",
            ),
            City(
                name = "Bordeaux",
                lat = "44.8404400",
                long = "-0.5805000",
            ),
            City(
                name = "Lyon",
                lat = "45.7484600",
                long = "4.8467100",
            ),
        )
    }
}
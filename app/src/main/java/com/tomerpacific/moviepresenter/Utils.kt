package com.tomerpacific.moviepresenter

class Utils {

    companion object {
        fun reverseDateFormat(date: String):String {
            val splitDate: List<String> = date.split("-")
            return splitDate[2] + "-" + splitDate[1] + "-" + splitDate[0]
        }
    }


}
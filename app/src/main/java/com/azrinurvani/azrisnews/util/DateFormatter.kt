package com.azrinurvani.azrisnews.util

import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale

fun dateFormatted(inputDateTime : String?) : String{
    val inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME
    val outFormatter = DateTimeFormatter
        .ofLocalizedDate(FormatStyle.LONG)
        .withLocale(Locale.getDefault())
    val dateString = try{
        val dateTime = OffsetDateTime.parse(inputDateTime,inputFormatter)
        dateTime.format(outFormatter)
    }catch (e : Exception){
        ""
    }
    return dateString
}
package com.example.githubbrowser

import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date

class DateAdapter : JsonAdapter<Date>() {
    val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")

    @FromJson
    override fun fromJson(reader: JsonReader) = try {
        val dateAsString = reader.nextString()
        formatter.parse(dateAsString)
    } catch (e: Exception) {
        Timber.d(e)
        null
    }

    @ToJson
    override fun toJson(writer: JsonWriter, value: Date?) {
        value?.let { writer.value(formatter.format(value)) }
    }
}
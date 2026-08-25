package dev.cordor.tablocation.github

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import java.io.IOException
import java.time.OffsetDateTime
import java.time.OffsetTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

object Converter {
    // Date-time helpers
    private val DATE_TIME_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_DATE_TIME)
        .appendOptional(DateTimeFormatter.ISO_OFFSET_DATE_TIME)
        .appendOptional(DateTimeFormatter.ISO_INSTANT)
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SX"))
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssX"))
        .appendOptional(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    fun parseDateTimeString(str: String): OffsetDateTime {
        return ZonedDateTime.from(DATE_TIME_FORMATTER.parse(str)).toOffsetDateTime()
    }

    private val TIME_FORMATTER: DateTimeFormatter = DateTimeFormatterBuilder()
        .appendOptional(DateTimeFormatter.ISO_TIME)
        .appendOptional(DateTimeFormatter.ISO_OFFSET_TIME)
        .parseDefaulting(ChronoField.YEAR, 2020)
        .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
        .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
        .toFormatter()
        .withZone(ZoneOffset.UTC)

    fun parseTimeString(str: String): OffsetTime {
        return ZonedDateTime.from(TIME_FORMATTER.parse(str)).toOffsetDateTime().toOffsetTime()
    }

    // Serialize/deserialize helpers
    @Throws(IOException::class)
    fun fromJsonString(json: String?): Array<Tags?>? {
        return objectReader.readValue(json)
    }

    @Throws(JsonProcessingException::class)
    fun toJsonString(obj: Array<Tags?>?): String? {
        return objectWriter.writeValueAsString(obj)
    }

    private var reader: ObjectReader? = null
    private var writer: ObjectWriter? = null

    private fun instantiateMapper() {
        val mapper = ObjectMapper()
        mapper.findAndRegisterModules()
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
        val module = SimpleModule()
        module.addDeserializer<OffsetDateTime?>(
            OffsetDateTime::class.java,
            object : JsonDeserializer<OffsetDateTime?>() {
                @Throws(IOException::class, JsonProcessingException::class)
                override fun deserialize(
                    jsonParser: JsonParser,
                    deserializationContext: DeserializationContext?
                ): OffsetDateTime {
                    val value = jsonParser.text
                    return parseDateTimeString(value)
                }
            })
        mapper.registerModule(module)
        reader = mapper.readerFor(Array<Tags>::class.java)
        writer = mapper.writerFor(Array<Tags>::class.java)
    }

    private val objectReader: ObjectReader
        get() {
            if (reader == null) instantiateMapper()
            return reader!!
        }

    private val objectWriter: ObjectWriter
        get() {
            if (writer == null) instantiateMapper()
            return writer!!
        }
}

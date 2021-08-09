package com.parentclass.springportfolio

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.util.*

@Configuration
class TodoMongoConfiguration {

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions? {
        return MongoCustomConversions(
            Arrays.asList(
                OffsetDateTimeReadConverter(),
                OffsetDateTimeWriteConverter()
            )
        )
    }

    internal class OffsetDateTimeWriteConverter : Converter<OffsetDateTime?, Date?> {
        /**
         * Convert the source object of type `S` to target type `T`.
         * @param source the source object to convert, which must be an instance of `S` (never `null`)
         * @return the converted object, which must be an instance of `T` (potentially `null`)
         * @throws IllegalArgumentException if the source cannot be converted to the desired target type
         */
        override fun convert(source: OffsetDateTime): Date? {
            return Date.from(source.toInstant().atZone(ZoneOffset.UTC).toInstant())
        }
    }

    internal class OffsetDateTimeReadConverter : Converter<Date?, OffsetDateTime?> {
        /**
         * Convert the source object of type `S` to target type `T`.
         * @param source the source object to convert, which must be an instance of `S` (never `null`)
         * @return the converted object, which must be an instance of `T` (potentially `null`)
         * @throws IllegalArgumentException if the source cannot be converted to the desired target type
         */
        override fun convert(source: Date): OffsetDateTime? {
            return source.toInstant().atOffset(ZoneOffset.UTC)
        }
    }
}

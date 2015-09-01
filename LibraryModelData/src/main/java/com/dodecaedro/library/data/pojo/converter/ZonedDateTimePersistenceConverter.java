package com.dodecaedro.library.data.pojo.converter;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimePersistenceConverter implements AttributeConverter<ZonedDateTime, Timestamp> {
  @Override
  public Timestamp convertToDatabaseColumn(ZonedDateTime attribute) {
    if (attribute == null) {
      return null;
    }

    return Timestamp.from(attribute.withZoneSameInstant(ZoneOffset.UTC).toInstant());
  }

  @Override
  public ZonedDateTime convertToEntityAttribute(Timestamp dbData) {
    if (dbData == null) {
      return null;
    }

    return ZonedDateTime.of(dbData.toLocalDateTime(), ZoneOffset.UTC);
  }
}

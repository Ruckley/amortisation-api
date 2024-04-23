package org.ldms.app.db;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.ldms.app.model.Money;

import java.math.BigDecimal;

@Converter(autoApply = true)
public class MoneyDbConverter implements AttributeConverter<Money, BigDecimal> {
    @Override
    public BigDecimal convertToDatabaseColumn(Money domainPrimitive) {
        return domainPrimitive.asBigDecimal();
    }

    @Override
    public Money convertToEntityAttribute(BigDecimal dbData) {
        return new Money(dbData);
    }
}
package org.ldms.app.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Represents monetary value using BigDecimal set to 2 decimal places
 */
@JsonSerialize(using = MoneySerializer.class)
@JsonDeserialize(using = MoneyDeserializer.class)
public class Money {

    public static Money ZERO = new Money(BigDecimal.ZERO);
    public static Money ONE = new Money(BigDecimal.ONE);

    public static Money POINT_1 = new Money(new BigDecimal("0.01"));
    public static Money NEG_POINT_1 = new Money(new BigDecimal("-0.01"));
    private final RoundingMode rm = RoundingMode.HALF_UP;
    private final BigDecimal value;

    public Money(BigDecimal value) {
        this.value = value.setScale(2, rm);
    }

    public Money(String value) {
        this.value = new BigDecimal(value).setScale(2, rm);
    }

    public BigDecimal asBigDecimal() {
        return value;
    }

    public Money add(Money other) {
        BigDecimal result = this.value.add(other.value);
        return new Money(result);
    }

    public Money subtract(Money other) {
        BigDecimal result = this.value.subtract(other.value);
        return new Money(result);
    }

    public Money multiply(Money other) {
        BigDecimal result = this.value.multiply(other.value);
        return new Money(result);
    }
    public Money multiply(BigDecimal other) {
        BigDecimal result = this.value.multiply(other);
        return new Money(result);
    }

    public Money divide(Money other) {
        BigDecimal result = this.value.divide(other.value, 2, rm);
        return new Money(result);
    }

    public Money pow(Integer n) {
        BigDecimal result = this.value.pow(n);
        return new Money(result);
    }

    public Integer compareTo(Money other){
        return this.value.compareTo(other.value);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Money money = (Money) o;
        return Objects.equals(value, money.value);
    }

    @Override
    public String toString() {
        return this.asBigDecimal().toString();
    }
}

class MoneySerializer extends JsonSerializer<Money> {
    @Override
    public void serialize(Money money, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(money.asBigDecimal());
    }
}

class MoneyDeserializer extends JsonDeserializer<Money> {
    @Override
    public Money deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String value = jsonParser.getValueAsString();
        return new Money(value);
    }
}
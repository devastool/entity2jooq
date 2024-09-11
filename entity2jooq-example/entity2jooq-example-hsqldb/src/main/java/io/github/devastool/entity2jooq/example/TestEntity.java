/*
 *    Copyright 2024 All entity2jooq contributors
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;
import io.github.devastool.entity2jooq.annotation.type.Type;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.util.Date;
import java.util.Objects;
import java.util.Random;
import java.util.UUID;

/**
 * Example entity, see tests.
 *
 * @author Sergey_Konovalov
 * @since 1.0.0
 */
@Table
@Schema("test_schema")
public class TestEntity extends TestEntityParent {
  private Short shortField;
  @Column
  private Integer intField;
  private Long longField;
  private BigDecimal bigDecimalField;
  private Float floatField;
  private Double doubleField;
  @Type("text")
  @Column(value = "entity_name")
  private String stringField;
  private LocalDate localDateField;
  private Date dateField;
  private java.sql.Date sqlDateField;
  private LocalTime localTimeField;
  private Time timeField;
  private OffsetTime offsetTimeField;
  private LocalDateTime localDateTimeField;
  private Timestamp timestampField;
  private OffsetDateTime offsetDateTimeField;
  private Boolean booleanField;
  private UUID uuidField;

  /**
   * Constructs new instance of {@link TestEntity}.
   */
  public TestEntity() {
    shortField = getRandomShort();
    intField = (int) shortField;
    longField = (long) shortField;
    bigDecimalField = BigDecimal.valueOf(shortField);
    floatField = (float) shortField;
    doubleField = (double) shortField;
    stringField = "TestEntity-" + shortField;
    localDateField = LocalDate.now();
    dateField = new Date();
    sqlDateField = java.sql.Date.valueOf(LocalDate.now());
    localTimeField = LocalTime.now();
    timeField = Time.valueOf(LocalTime.now());
    offsetTimeField = OffsetTime.now();
    localDateTimeField = LocalDateTime.now();
    timestampField = Timestamp.valueOf(LocalDateTime.now());
    offsetDateTimeField = OffsetDateTime.now();
    booleanField = false;
    uuidField = UUID.randomUUID();
  }

  /**
   * Constructs new instance of {@link TestEntity}.
   */
  public TestEntity(
      Short shortField,
      Integer intField,
      Long longField,
      BigDecimal bigDecimalField,
      Float floatField,
      Double doubleField,
      String stringField,
      LocalDate localDateField,
      Date dateField,
      java.sql.Date sqlDateField,
      LocalTime localTimeField,
      Time timeField,
      OffsetTime offsetTimeField,
      LocalDateTime localDateTimeField,
      Timestamp timestampField,
      OffsetDateTime offsetDateTimeField,
      Boolean booleanField,
      UUID uuidField
  ) {
    this.shortField = shortField;
    this.intField = intField;
    this.longField = longField;
    this.bigDecimalField = bigDecimalField;
    this.floatField = floatField;
    this.doubleField = doubleField;
    this.stringField = stringField;
    this.localDateField = localDateField;
    this.dateField = dateField;
    this.sqlDateField = sqlDateField;
    this.localTimeField = localTimeField;
    this.timeField = timeField;
    this.offsetTimeField = offsetTimeField;
    this.localDateTimeField = localDateTimeField;
    this.timestampField = timestampField;
    this.offsetDateTimeField = offsetDateTimeField;
    this.booleanField = booleanField;
    this.uuidField = uuidField;
  }

  public Short getShortField() {
    return shortField;
  }

  public void setShortField(Short shortField) {
    this.shortField = shortField;
  }

  public Integer getIntField() {
    return intField;
  }

  public void setIntField(Integer intField) {
    this.intField = intField;
  }

  public Long getLongField() {
    return longField;
  }

  public void setLongField(Long longField) {
    this.longField = longField;
  }

  public BigDecimal getBigDecimalField() {
    return bigDecimalField;
  }

  public void setBigDecimalField(BigDecimal bigDecimalField) {
    this.bigDecimalField = bigDecimalField;
  }

  public Float getFloatField() {
    return floatField;
  }

  public void setFloatField(Float floatField) {
    this.floatField = floatField;
  }

  public Double getDoubleField() {
    return doubleField;
  }

  public void setDoubleField(Double doubleField) {
    this.doubleField = doubleField;
  }

  public String getStringField() {
    return stringField;
  }

  public void setStringField(String stringField) {
    this.stringField = stringField;
  }

  public LocalDate getLocalDateField() {
    return localDateField;
  }

  public void setLocalDateField(LocalDate localDateField) {
    this.localDateField = localDateField;
  }

  public Date getDateField() {
    return dateField;
  }

  public void setDateField(Date dateField) {
    this.dateField = dateField;
  }

  public java.sql.Date getSqlDateField() {
    return sqlDateField;
  }

  public void setSqlDateField(java.sql.Date sqlDateField) {
    this.sqlDateField = sqlDateField;
  }

  public LocalTime getLocalTimeField() {
    return localTimeField;
  }

  public void setLocalTimeField(LocalTime localTimeField) {
    this.localTimeField = localTimeField;
  }

  public Time getTimeField() {
    return timeField;
  }

  public void setTimeField(Time timeField) {
    this.timeField = timeField;
  }

  public OffsetTime getOffsetTimeField() {
    return offsetTimeField;
  }

  public void setOffsetTimeField(OffsetTime offsetTimeField) {
    this.offsetTimeField = offsetTimeField;
  }

  public LocalDateTime getLocalDateTimeField() {
    return localDateTimeField;
  }

  public void setLocalDateTimeField(LocalDateTime localDateTimeField) {
    this.localDateTimeField = localDateTimeField;
  }

  public Timestamp getTimestampField() {
    return timestampField;
  }

  public void setTimestampField(Timestamp timestampField) {
    this.timestampField = timestampField;
  }

  public OffsetDateTime getOffsetDateTimeField() {
    return offsetDateTimeField;
  }

  public void setOffsetDateTimeField(OffsetDateTime offsetDateTimeField) {
    this.offsetDateTimeField = offsetDateTimeField;
  }

  public Boolean getBooleanField() {
    return booleanField;
  }

  public void setBooleanField(Boolean booleanField) {
    this.booleanField = booleanField;
  }

  public UUID getUuidField() {
    return uuidField;
  }

  public void setUuidField(UUID uuidField) {
    this.uuidField = uuidField;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (other == null || getClass() != other.getClass()) {
      return false;
    }
    TestEntity entity = (TestEntity) other;
    return Objects.equals(shortField, entity.shortField)
        && Objects.equals(intField, entity.intField)
        && Objects.equals(longField, entity.longField)
        && Objects.equals(bigDecimalField, entity.bigDecimalField)
        && Objects.equals(floatField, entity.floatField)
        && Objects.equals(doubleField, entity.doubleField)
        && Objects.equals(stringField, entity.stringField)
        && Objects.equals(localDateField, entity.localDateField)
        && Objects.equals(dateField, entity.dateField)
        && Objects.equals(sqlDateField, entity.sqlDateField)
        && Objects.equals(localTimeField, entity.localTimeField)
        && Objects.equals(timeField, entity.timeField)
        && Objects.equals(offsetTimeField, entity.offsetTimeField)
        && Objects.equals(localDateTimeField, entity.localDateTimeField)
        && Objects.equals(timestampField, entity.timestampField)
        && Objects.equals(offsetDateTimeField, entity.offsetDateTimeField)
        && Objects.equals(booleanField, entity.booleanField)
        && Objects.equals(uuidField, entity.uuidField);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        shortField,
        intField,
        longField,
        bigDecimalField,
        floatField,
        doubleField,
        stringField,
        localDateField,
        dateField,
        sqlDateField,
        localTimeField,
        timeField,
        offsetTimeField,
        localDateTimeField,
        timestampField,
        offsetDateTimeField,
        booleanField,
        uuidField
    );
  }

  private static short getRandomShort() {
    return (short) new Random().nextInt(Short.MAX_VALUE - 1);
  }
}

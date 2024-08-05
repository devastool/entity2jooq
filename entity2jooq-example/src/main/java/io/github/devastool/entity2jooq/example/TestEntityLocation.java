package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Embedded;

/**
 * Example embedded entity.
 *
 * @since 1.0.0
 * @author Sergey_Konovalov
 */
@Embedded
public class TestEntityLocation {
private String country;

  public TestEntityLocation() {
  }

  public TestEntityLocation(String country) {
    this.country = country;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }
}

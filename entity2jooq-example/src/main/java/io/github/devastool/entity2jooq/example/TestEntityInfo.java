
package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Embedded;
import java.util.Objects;

/**
 * Example entity (embedded object).
 *
 * @author Sergey_Konovalov
 * @since 0.0.1
 */
@Embedded
public class TestEntityInfo {
  @Column("entity_version")
  private String version;
  private TestEntityLocation location;

  public TestEntityInfo() {
  }

  public TestEntityInfo(String version, TestEntityLocation location) {
    this.version = version;
    this.location = location;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public TestEntityLocation getLocation() {
    return location;
  }

  public void setLocation(TestEntityLocation location) {
    this.location = location;
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }
    if (object == null || getClass() != object.getClass()) {
      return false;
    }
    TestEntityInfo that = (TestEntityInfo) object;
    return Objects.equals(version, that.version) && Objects.equals(location,
        that.location);
  }

  @Override
  public int hashCode() {
    return Objects.hash(version, location);
  }
}

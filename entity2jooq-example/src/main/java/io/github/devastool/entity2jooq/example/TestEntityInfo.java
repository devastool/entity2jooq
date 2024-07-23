
package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Column;
import java.util.Objects;

/**
 * Example entity (embedded object).
 *
 * @author Sergey_Konovalov
 * @since 0.0.1
 */
public class TestEntityInfo {
  @Column("entity_version")
  private String version;

  public TestEntityInfo() {
  }

  public TestEntityInfo(String version) {
    this.version = version;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
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
    return Objects.equals(version, that.version);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(version);
  }
}

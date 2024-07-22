package io.github.devastool.entity2jooq.example;

import io.github.devastool.entity2jooq.annotation.Column;
import io.github.devastool.entity2jooq.annotation.Schema;
import io.github.devastool.entity2jooq.annotation.Table;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Subclass of TestEntity for testing inheritance.
 *
 * @since 0.0.1
 * @author Filkov_Artem
 */
@Table
@Schema("test_schema")
public class InheritanceTestEntity extends TestEntity {

    @Column(value = "entity_description", type = "varchar")
    private String description;

    public InheritanceTestEntity() {
        super();
    }

    public InheritanceTestEntity(Integer id, String name, Timestamp insertTime, String description) {
        super(id, name, insertTime);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object other) {
        if (!super.equals(other)) {
            return false;
        }
        if (getClass() != other.getClass()) {
            return false;
        }
        InheritanceTestEntity that = (InheritanceTestEntity) other;
        return Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), description);
    }
}
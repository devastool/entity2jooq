package io.github.devastool.entity2jooq.codegen.generate.params;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

/**
 * Tests of {@link LinkPair}.
 *
 * @author Sergey_Konovalov
 */
class LinkPairTest {
  @Test
  public void equalsSameObjectTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    assertEquals(linkPair1, linkPair1);
  }

  @Test
  public void equalsDifferentPrimaryTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    LinkPair linkPair2 = new LinkPair("C", "B");
    assertNotEquals(linkPair1, linkPair2);
  }

  @Test
  public void equalsDifferentSecondaryTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    LinkPair linkPair2 = new LinkPair("A", "C");
    assertNotEquals(linkPair1, linkPair2);
  }

  @Test
  public void equalsNullTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    LinkPair linkPair2 = null;
    assertNotEquals(linkPair1, linkPair2);
  }

  @Test
  public void equalsDifferentTypeTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    assertNotEquals(linkPair1, "");
  }

  @Test
  public void hashCodeSameValuesTest() {
    LinkPair linkPair1 = new LinkPair("A", "B");
    LinkPair linkPair2 = new LinkPair("A", "B");
    assertEquals(linkPair1.hashCode(), linkPair2.hashCode());
  }
}
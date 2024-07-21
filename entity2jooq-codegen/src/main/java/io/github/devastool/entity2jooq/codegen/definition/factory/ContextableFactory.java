package io.github.devastool.entity2jooq.codegen.definition.factory;

public abstract class ContextableFactory {
  /**
   * Factory context.
   */
  private final FactoryContext<?> context;

  /**
   * Constructor.
   * @param context factory context.
   */
    public ContextableFactory(FactoryContext<?> context) {
      this.context = context;
    }

  /**
   * Getter
   * @return context.
   */
  public FactoryContext<?> getContext() {
    return context;
  }
}

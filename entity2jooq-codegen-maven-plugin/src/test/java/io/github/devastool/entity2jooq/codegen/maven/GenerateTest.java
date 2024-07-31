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

package io.github.devastool.entity2jooq.codegen.maven;

import io.github.devastool.entity2jooq.codegen.Entity2JooqJavaGenerator;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.jooq.meta.jaxb.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link Generate}.
 *
 * @author Andrey_Yurzanov
 */
class GenerateTest {
  private static final String DIALECT = "dialect";
  private static final String CLASS_NAME = "className";
  private static final String PACKAGE_NAME = "packageName";
  private static final String TARGET = "target";
  private static final String DIALECT_DEFAULT = "";
  private static final String CLASS_NAME_DEFAULT =
      Entity2JooqJavaGenerator.class.getCanonicalName();
  private static final String PACKAGE_NAME_DEFAULT = "org.jooq.generated";
  private static final String TARGET_DEFAULT = TARGET + "/generated-sources/jooq";

  @Test
  void getGenerateTargetSuccessTest() {
    Generate generate = new Generate();
    generate.setDialect(DIALECT);
    generate.setClassName(CLASS_NAME);
    generate.setPackageName(PACKAGE_NAME);
    generate.setTarget(TARGET);

    Target target = generate.getGenerateTarget();
    Assertions.assertEquals(PACKAGE_NAME, target.getPackageName());
    Assertions.assertEquals(TARGET, target.getDirectory());
  }

  @Test
  void initSuccessTest() {
    Generate generate = new Generate();
    generate.setDialect(DIALECT);
    generate.setClassName(CLASS_NAME);
    generate.setPackageName(PACKAGE_NAME);
    generate.setTarget(TARGET);

    generate.init(new MavenProject());
    Assertions.assertEquals(DIALECT, generate.getDialect());
    Assertions.assertEquals(CLASS_NAME, generate.getClassName());
    Assertions.assertEquals(PACKAGE_NAME, generate.getPackageName());
    Assertions.assertEquals(TARGET, generate.getTarget());
  }

  @Test
  void initSuccessDefaultTest() {
    Build build = new Build();
    build.setDirectory(TARGET);

    MavenProject project = new MavenProject();
    project.setBuild(build);

    Generate generate = new Generate();
    generate.init(project);
    Assertions.assertEquals(DIALECT_DEFAULT, generate.getDialect());
    Assertions.assertEquals(CLASS_NAME_DEFAULT, generate.getClassName());
    Assertions.assertEquals(PACKAGE_NAME_DEFAULT, generate.getPackageName());
    Assertions.assertEquals(TARGET_DEFAULT, generate.getTarget());
  }
}
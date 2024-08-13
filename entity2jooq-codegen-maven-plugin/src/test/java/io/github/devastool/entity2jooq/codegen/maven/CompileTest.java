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

import java.util.List;
import org.apache.maven.artifact.DependencyResolutionRequiredException;
import org.apache.maven.model.Build;
import org.apache.maven.project.MavenProject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Tests of {@link Compile}.
 *
 * @author Evgeniy_Gerasimov
 */
class CompileTest {
  private static final String TARGET = "target";

  @Test
  void initSuccessDefaultTest() throws DependencyResolutionRequiredException {
    Build build = new Build();
    build.setOutputDirectory(TARGET);

    MavenProject project = new MavenProject();
    project.setBuild(build);

    var compile = new Compile();
    compile.init(project);
    Assertions.assertEquals(TARGET, compile.getTarget());
    Assertions.assertEquals(TARGET, compile.getClasspathFiles().get(0).getName());

    compile = new Compile();
    compile.setClasspath(List.of("testPath"));
    compile.init(project);

    Assertions.assertEquals(2, compile.getClasspath().size());
  }
}
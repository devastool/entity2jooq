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

import static io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor.JAVA_FILE_EXT;

import io.github.devastool.entity2jooq.codegen.Entity2JooqDatabase;
import io.github.devastool.entity2jooq.codegen.filesystem.ExtFileVisitor;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;
import org.jooq.codegen.GenerationTool;
import org.jooq.meta.jaxb.Configuration;
import org.jooq.meta.jaxb.Database;
import org.jooq.meta.jaxb.Generator;
import org.jooq.meta.jaxb.Property;

/**
 * Maven plugin for code generating.
 *
 * @since 1.0.0
 * @author Andrey_Yurzanov
 */
@Mojo(
    name = "entity2jooq-generate",
    defaultPhase = LifecyclePhase.PROCESS_CLASSES,
    requiresDependencyResolution = ResolutionScope.COMPILE
)
public class CodegenPlugin extends AbstractMojo {
  @Parameter(property = "project", required = true, readonly = true)
  private MavenProject project;
  @Parameter(property = "generate")
  private Generate generate;
  @Parameter(property = "compile")
  private Compile compile;

  private static final String CLASSES_PROPERTY_KEY = "classes";
  private static final String TEST_CLASSES_PROPERTY_KEY = "testClasses";

  @Override
  public void execute() throws MojoExecutionException, MojoFailureException {
    try {
      generate.init(project);
      compile.init(project);

      // Step 1. Generating source code
      Generator generator = new Generator();
      generator.setName(generate.getClassName());
      generator.setDatabase(getDatabase());
      generator.setTarget(generate.getGenerateTarget());

      Configuration configuration = new Configuration();
      configuration.setGenerator(generator);

      GenerationTool.generate(configuration);

      // Step 2. Compiling source code
      ExtFileVisitor visitor = new ExtFileVisitor(JAVA_FILE_EXT);
      Files.walkFileTree(Paths.get(generate.getTarget()), visitor);

      JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
      StandardJavaFileManager manager = compiler.getStandardFileManager(null, null, null);
      manager.setLocation(StandardLocation.CLASS_OUTPUT, compile.getTargetLocation());
      manager.setLocation(StandardLocation.CLASS_PATH, compile.getClasspathFiles());

      Collection<PathJavaFileObject> sourceCodes = visitor.getFiltered(PathJavaFileObject::new);
      CompilationTask task = compiler.getTask(null, manager, null, null, null, sourceCodes);
      task.call();
    } catch (Exception exception) {
      throw new RuntimeException(exception);
    }
  }

  private Database getDatabase() {
    Build build = project.getBuild();
    Property classes = new Property();
    classes.setKey(CLASSES_PROPERTY_KEY);
    classes.setValue(build.getOutputDirectory());

    Property testClasses = new Property();
    testClasses.setKey(TEST_CLASSES_PROPERTY_KEY);
    testClasses.setValue(build.getTestOutputDirectory());

    Database database = new Database();
    database.setName(Entity2JooqDatabase.class.getCanonicalName());
    database.setProperties(Arrays.asList(classes, testClasses));
    return database;
  }
}

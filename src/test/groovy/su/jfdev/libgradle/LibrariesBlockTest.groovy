package su.jfdev.libgradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationContainer
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.junit.Assert
import org.junit.Test
import su.jfdev.libbinder.BindProvider
import su.jfdev.libbinder.Binding

import static org.mockito.Matchers.any
import static org.mockito.Matchers.eq
import static org.mockito.Mockito.mock
import static org.mockito.Mockito.when
/**
 * Jamefrus and his team on 31.05.2016.
 */
class LibrariesBlockTest {
    private BindProvider provider

    @Test
    public void 'can invoke methods with configuration\'s name'() throws Exception {
        Project project = mockProject()
        provider = Binding.parse([any: "group:name:version"])
        def block = new LibrariesBlock(project: project, provider: provider)
        block.compile("any"){}
    }

    public Project mockProject() {
        def project = mock(Project)

        def container = mockConfContainer()
        when(project.configurations).thenReturn(container)

        def handler = mockDependenciesHandler()
        when(project.dependencies).thenReturn(handler)
        project
    }

    private DependencyHandler mockDependenciesHandler() {
        def handler = mock(DependencyHandler)
        def anyClosure = any(Closure)
        def anyNotation = any()
        when(handler.add(eq("compile"), anyNotation, anyClosure)).then {
            def notation = it.getArgument(1)
            def libMap = provider.library("any").map
            Assert.assertEquals(notation, libMap)
            return null
        }
        handler
    }

    private ConfigurationContainer mockConfContainer() {
        def configurations = mock(ConfigurationContainer)

        def configuration = mockConfiguration()
        when(configurations.findByName(eq("compile"))).thenReturn(configuration)
        configurations
    }

    private Configuration mockConfiguration() {
        Configuration compileConfiguration = mock(Configuration)
        when(compileConfiguration.name).thenReturn("compile")
        compileConfiguration
    }
}

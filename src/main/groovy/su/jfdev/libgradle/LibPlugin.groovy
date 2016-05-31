package su.jfdev.libgradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.MavenPlugin
import su.jfdev.libbinder.BindProvider
import su.jfdev.libbinder.Binding

/**
 * Jamefrus and his team on 29.05.2016.
 */
class LibPlugin implements Plugin<Project> {

    Project project

    @Override
    void apply(Project project) {
        project.plugins.apply(MavenPlugin)
        this.project = project
        improveBinding()
        project.repositories.jcenter()
        addBlock("reposources") {
            new SourcesBlock(project: project, provider: it)
        }
        addBlock("libraries") {
            new LibrariesBlock(project: project, provider: it)
        }
    }

    public void improveBinding() {
        project.extensions.extraProperties.binder = Binding.INSTANCE
    }

    private void addBlock(String name, Closure<Object> blockBuilder) {
        def emptyProvider = Binding.parse(Collections.emptyMap())
        project.extensions.extraProperties[name] = { BindProvider provider = emptyProvider, Closure block ->
            block.delegate = blockBuilder(provider)
            block.resolveStrategy = Closure.DELEGATE_FIRST
            block.run()
        }
    }
}

package su.jfdev.libgradle

import org.codehaus.groovy.runtime.InvokerHelper
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.Dependency
import su.jfdev.libbinder.BindProvider
import su.jfdev.libbinder.items.Library

/**
 * Jamefrus and his team on 30.05.2016.
 */
class LibrariesBlock {
    Project project
    BindProvider provider;

    def methodMissing(String name, args) {
        def array = InvokerHelper.asArray(args)
        Configuration configuration = project.configurations.findByName(name);
        if (configuration == null) throw new MissingMethodException(name, this.getClass(), array)

        def doAdd = { Object library, Closure closure = {} ->
            Iterable<Library> libraries = libraries(library)
            Dependency last = null
            for (lib in libraries) {
                last = project.dependencies.add(name, lib.map, closure)
            }
            return last
        }

        return InvokerHelper.invokeClosure(doAdd, args)
    }

    private Iterable<Library> libraries(Object library) {
        if (library instanceof Library) return [library]
        def alias = library.toString()
        try {
            return [provider.library(alias)]
        } catch (Exception ignored) {
            return provider.libraries(alias)
        }
    }

}

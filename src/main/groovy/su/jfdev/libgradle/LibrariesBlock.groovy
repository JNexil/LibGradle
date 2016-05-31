package su.jfdev.libgradle

import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import su.jfdev.libbinder.BindProvider
import su.jfdev.libbinder.MissingBindException
import su.jfdev.libbinder.items.Library

/**
 * Jamefrus and his team on 30.05.2016.
 */
class LibrariesBlock extends GroovyObjectSupport {
    Project project
    BindProvider from

    @Override
    Object invokeMethod(String name, Object args) {
        try {
            return super.invokeMethod(name, args)
        } catch (MissingMethodException e) {
            def configuration = project.configurations.findByName(name)
            if (configuration == null) throw e
            try {
                addBind(configuration, args)
            } catch (ArrayIndexOutOfBoundsException | ClassCastException ignored) {
                throw e
            }
        }
        return null
    }

    private void addBind(Configuration configuration, Object args) {
        if (args instanceof String) addBind(configuration, args)
        else if (args instanceof Object[]) {
            String name = args[0] as String
            if(args.length == 1) addBind(configuration, name)
            else addBind(configuration, name, args[1] as Closure)
        }
        else throw new ClassCastException()
    }

    private void addBind(Configuration configuration, String name, Closure closure = {}) {
        getBind(name).forEach {
            dependency(configuration, it, closure)
        }
    }

    private Iterable<Library> getBind(String name) {
        try {
            return [from.library(name)]
        } catch (MissingBindException ignored) {
            return from.libraries(name)
        }
    }

    private void dependency(Configuration configuration, Library lib, Closure closure = {}) {
        project.dependencies.add(configuration.name, lib.map, closure)
    }
}

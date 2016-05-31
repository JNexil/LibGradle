package su.jfdev.libgradle

import org.gradle.api.Project
import su.jfdev.libbinder.BindProvider
import su.jfdev.libbinder.MissingBindException
import su.jfdev.libbinder.items.Source

/**
 * Jamefrus and his team on 30.05.2016.
 */
class SourcesBlock {
    Project project
    BindProvider from

    void from(String alias) {
        sources(alias).forEach { source ->
            project.repositories.maven {
                url = source.url
                credentials {
                    username = source.username
                    password = source.password
                }
            }
        }
    }

    void to(String alias, Closure customize = {}) {
        sources(alias).forEach {
            addTO(it, customize)
        }
    }

    private Iterable<Source> sources(String name) {
        try {
            return [from.source(name)]
        } catch (MissingBindException ignored) {
            return from.sources(name)
        }
    }

    private void addTO(Source source, Closure customize) {
        project.uploadArchives {
            repositories.mavenDeployer {
                customize.setDelegate(getThisObject())
                customize.setResolveStrategy(DELEGATE_FIRST)
                customize.run()
                repository(url: source.url) {
                    authentication(userName: source.password, password: source.username)
                }
            }
        }
    }
}

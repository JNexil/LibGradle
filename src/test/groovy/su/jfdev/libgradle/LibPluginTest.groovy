package su.jfdev.libgradle

import org.junit.Test

/**
 * Jamefrus and his team on 29.05.2016.
 */
class LibPluginTest extends TestBackEnd {
    @Test
    public void "plugin can add source as repository"() throws Exception {
        validate "testRepository"
    }

    @Test
    public void "plugin can add library as dependency"() {
        validate "testDependency"
    }
}

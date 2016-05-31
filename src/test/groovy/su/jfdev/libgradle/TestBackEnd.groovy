package su.jfdev.libgradle

import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TemporaryFolder

/**
 * Jamefrus and his team on 29.05.2016.
 */
abstract class TestBackEnd {
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder()
    private File temp

    @Before
    public void setUp() throws Exception {
        temp = temporaryFolder.newFolder("TestKit")
    }

    protected void validate(String fileName) {
        copy(fileName + ".gradle", "build.gradle")
        copy("binding.gconfig")
        assert GradleRunner.create()
                .withProjectDir(temp)
                .withArguments("testing")
                .withDebug(true)
                .withPluginClasspath()
                .build().task(":testing").outcome == TaskOutcome.SUCCESS
    }

    private void copy(String fileName, String to = fileName) {
        def file = new File(temp, to)
        file << LibPluginTest.classLoader.getResourceAsStream(fileName).text
    }
}

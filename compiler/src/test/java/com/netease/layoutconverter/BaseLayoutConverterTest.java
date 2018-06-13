package com.netease.layoutconverter;


import com.netease.layoutconverter.codegen.LayoutConverter;
import com.netease.layoutconverter.codegen.NotSupportException;

import org.apache.commons.io.IOUtils;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import kotlin.text.Charsets;

import static kotlin.io.FilesKt.readText;
import static org.junit.Assert.assertEquals;

public class BaseLayoutConverterTest {
    @Rule
    public TestName name = new TestName();

    protected void doLayoutTest() throws IOException, NotSupportException {
        String testName = name.getMethodName();
        if (!testName.startsWith("test")) {
            throw new IllegalStateException("Test name must start with a 'test' prefix");
        }

        ResourceParser.INSTANCE.parse();

        String path = "/layout/" + testDir(testName.substring("test".length())) + "/";
        final Map<String, String> map = map(path);

        String actual = LayoutConverter.INSTANCE.genCode("com.netease.layoutconverter.test", "Layout", map.get("layout.xml"), map);
        String expected = IOUtils.toString(getClass().getResourceAsStream(path + "Layout.java"), "utf-8");

        System.out.println(actual);

        assertEquals(expected.replace("\r\n", "\n"), actual);
    }

    private String testDir(String original) {
        if (original.isEmpty()) { return original; }
        return Character.toLowerCase(original.charAt(0)) + original.substring(1);
    }

    private Map<String, String> map(String path) throws IOException {
        final Map<String, String> map = new HashMap<>();

        final File jarFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());

        if (jarFile.isFile()) {  // Run with JAR file
            final JarFile jar = new JarFile(jarFile);
            final Enumeration<JarEntry> entries = jar.entries(); //gives ALL entries in jar
            while (entries.hasMoreElements()) {
                final String name = entries.nextElement().getName();
                if (name.startsWith(path + "/")) { //filter according to the path
                    map.put(
                            name.substring(name.lastIndexOf("/") + 1, name.length()),
                            IOUtils.toString(BaseLayoutConverterTest.class.getResourceAsStream(name), "utf-8")
                    );
                }
            }
            jar.close();
        } else { // Run with IDE
            final URL url = BaseLayoutConverterTest.class.getResource(path);
            if (url != null) {
                try {
                    final File files = new File(url.toURI());
                    for (File file : files.listFiles()) {
                        if (file.getName().endsWith(".xml")) {
                            map.put(file.getName(), readText(file, Charsets.UTF_8));
                        }
                    }
                } catch (URISyntaxException ignored) {}
            }
        }

        return map;
    }
}

/*
 * Copyright 2010-2012 Kevin Seim
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.beanio.parser.fixedlength.countMode;

import org.beanio.BeanReader;
import org.beanio.StreamFactory;
import org.beanio.internal.util.Settings;
import org.beanio.parser.ParserTest;
import org.junit.*;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.nio.charset.Charset;

public class FixedLengthCountModeTest extends ParserTest {

    private StreamFactory factory;

    @BeforeClass
    public static void setUpClass() {
        clearSettings();
        System.setProperty("org.beanio.configuration", "org/beanio/parser/fixedlength/countMode/beanio_test.properties");
        Settings.getInstance();
    }

    @AfterClass
    public static void tearDownClass() {
        clearSettings();
        System.clearProperty("org.beanio.configuration");
        Settings.getInstance();
    }

    private static void clearSettings() {
        try {
            Field settings = Settings.class.getDeclaredField("settings");
            settings.setAccessible(true);
            settings.set(null, null);
        } catch (ReflectiveOperationException e) {
            throw new IllegalArgumentException("Could not access settings field", e);
        }
    }

    @Before
    public void setup() throws Exception {
        factory = newStreamFactory("fixedlength.xml");
    }

    @Test
    public void testByteLengthValidation() {
        BeanReader in = factory.createReader("f", new InputStreamReader(getClass().getResourceAsStream("f.txt"), Charset.forName("UTF-8")));
        try {
            // Using Shift-JIS, all japanese characters use 2 bytes. ASCII characters are 1 byte.
            // The first line is not valid, it is only 6 bytes long
            assertFieldError(in, 1, "record", "first", "ハロー", "Invalid field length, expected 10 bytes");
            // The second line is not valid, the second field exceeds max length of 4 bytes
            assertFieldError(in, 2, "record", "second", "ハロー    ", "Maximum field length is 4 bytes");
            // The third line is perfectly fine
        }
        finally {
            in.close();
        }
    }

}

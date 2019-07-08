package org.openpcm.util;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openpcm.utils.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ObjectUtilTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtilTest.class);

    @DisplayName("Ensure ObjectWriter correctly serializes objects")
    @Test
    public void test_print_worksCorrectly() {
        final Tester tester = new Tester() {

            private String name = "test";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        };

        final byte[] expected = "{\"name\":\"test\"}".getBytes();
        LOGGER.info("expected: {}. actual: {}", expected, ObjectUtil.print(tester).getBytes());
        assertArrayEquals("{\"name\":\"test\"}".getBytes(), ObjectUtil.print(tester).getBytes(), "json is incorrect");
    }

    @DisplayName("Ensure ObjectWriter pretty print correctly serializes objects")
    @Test
    public void test_prettyPrint_worksCorrectly() {
        final Tester tester = new Tester() {

            private String name = "test";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        };

        assertFalse(ObjectUtil.prettyPrint(tester) == null, "pretty print should not be null");
        assertFalse(ObjectUtil.prettyPrint(tester).equals(""), "pretty print should not be empty string");
        assertFalse(ObjectUtil.prettyPrint(tester).equals("{}"), "pretty print should not be empty json object");
    }

}

interface Tester {

}
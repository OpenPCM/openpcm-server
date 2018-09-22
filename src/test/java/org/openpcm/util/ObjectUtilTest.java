package org.openpcm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openpcm.utils.ObjectUtil;

public class ObjectUtilTest {

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

        assertEquals("{\"name\":\"test\"}", ObjectUtil.print(tester), "json is incorrect");
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

        assertEquals("{\r\n  \"name\" : \"test\"\r\n}", ObjectUtil.prettyPrint(tester), "json is incorrect");
    }

}

interface Tester {

}
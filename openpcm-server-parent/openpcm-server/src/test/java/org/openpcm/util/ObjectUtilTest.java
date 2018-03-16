package org.openpcm.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.openpcm.utils.ObjectUtil;

public class ObjectUtilTest {

    @Test
    public void test_print_worksCorrectly() {
        Tester tester = new Tester() {

            private String name = "test";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        };

        assertEquals("json is incorrect", "{\"name\":\"test\"}", ObjectUtil.print(tester));
    }

    @Test
    public void test_prettyPrint_worksCorrectly() {
        Tester tester = new Tester() {

            private String name = "test";

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

        };

        assertEquals("json is incorrect", "{\r\n  \"name\" : \"test\"\r\n}", ObjectUtil.prettyPrint(tester));
    }

}

interface Tester {

}
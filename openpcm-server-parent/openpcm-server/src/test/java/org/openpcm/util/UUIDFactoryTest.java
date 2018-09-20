package org.openpcm.util;

import static org.junit.Assert.assertFalse;

import org.junit.Test;
import org.openpcm.utils.UUIDFactory;
import org.springframework.util.StringUtils;

public class UUIDFactoryTest {

    @Test
    public void test_opId_neverNull() {
        assertFalse("opId should never be null", StringUtils.isEmpty(UUIDFactory.opId()));
    }

}

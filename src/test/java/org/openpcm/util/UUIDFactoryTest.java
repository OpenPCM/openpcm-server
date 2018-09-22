package org.openpcm.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.openpcm.utils.UUIDFactory;

public class UUIDFactoryTest {

    @RepeatedTest(value = 10, name = "{displayName} {currentRepetition}/{totalRepetitions}")
    @DisplayName("Ensure UUID generation is not flaky")
    public void test_opId_trulyUnique() {
        final List<String> uuids = new ArrayList<>();
        for (int i = 0; i < 10000; i++) {
            uuids.add(UUIDFactory.opId());
        }

        assertEquals(uuids.size(), uuids.stream().collect(Collectors.toSet()).size(), "collections should have same number of elements");
    }

}

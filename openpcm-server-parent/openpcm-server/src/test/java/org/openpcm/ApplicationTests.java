package org.openpcm;

import org.junit.experimental.categories.Category;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openpcm.annotation.IntegrationTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@Category(IntegrationTest.class)
@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ApplicationTests {

    @Test
    public void test_main_contextLoads() {
    }
}

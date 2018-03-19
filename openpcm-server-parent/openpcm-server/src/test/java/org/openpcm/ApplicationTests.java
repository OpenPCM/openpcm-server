package org.openpcm;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = { OpenPCMApplication.class })
public class ApplicationTests {

    @Test
    public void test_main_contextLoads() {
    }
}

package org.openpcm;

import org.openpcm.model.OpenPCMEvent;
import org.openpcm.model.OpenPCMEvent.EventType;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories("org.openpcm.dao")
public class Application {

    public static void main(String[] args) throws Exception {
        ConfigurableApplicationContext context = SpringApplication.run(Application.class, args);
        context.publishEvent(new OpenPCMEvent(Application.class, EventType.APP_STARTED));

    }
}

package tn.esprit.foyer.Schedular;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Slf4j
public class TestScheduler{

    @Scheduled(fixedRate = 2000)
    void affiche(){
        log.info("Bonjour");
    }
}

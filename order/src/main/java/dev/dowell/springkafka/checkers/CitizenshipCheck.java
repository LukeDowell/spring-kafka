package dev.dowell.springkafka.checkers;

import org.springframework.stereotype.Component;

@Component
public class CitizenshipCheck implements Checks {
    @Override
    public boolean isValid() {
        return false;
    }
}

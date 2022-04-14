package dev.dowell.springkafka.checkers;

import java.util.Collections;

public class LowRiskProfile extends Profile {

    public LowRiskProfile() {
        this.checks = Collections.singleton(new CitizenshipCheck());
    }

    @Override
    public boolean shouldValidate() {
        return false;
    }
}

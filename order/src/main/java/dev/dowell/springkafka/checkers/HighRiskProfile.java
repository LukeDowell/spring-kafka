package dev.dowell.springkafka.checkers;

import java.util.Collections;

public class HighRiskProfile extends Profile {
    public HighRiskProfile() {
        this.checks = Collections.singleton(new CitizenshipCheck());
    }

    @Override
    public boolean shouldValidate() {
        return this.checks.stream().allMatch((c) -> c.isValid(myContext));
    }
}

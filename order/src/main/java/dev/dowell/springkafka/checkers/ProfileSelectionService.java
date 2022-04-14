package dev.dowell.springkafka.checkers;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileSelectionService {

    private final List<Profile> profiles;

    public ProfileSelectionService(List<Profile> profiles) {
        this.profiles = profiles;
    }

    public Profile getProfile() {
        // whatever criteria
        return profiles.stream().filter(Profile::shouldValidate)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Can't find a profile!"));
    }
}

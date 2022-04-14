package dev.dowell.springkafka.checkers;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public abstract class Profile {
    protected List<Checks> checks;

    public abstract boolean shouldValidate();
}

package dev.dowell.springkafka;

import lombok.*;

import javax.validation.constraints.NotEmpty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.PRIVATE)
public class NewMember {

    @NotEmpty
    private String referralId;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;
}

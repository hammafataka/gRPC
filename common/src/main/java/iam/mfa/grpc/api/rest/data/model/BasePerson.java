package iam.mfa.grpc.api.rest.data.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 23:36
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BasePerson {
    @NotEmpty(message = "name of person cannot be null for person ")
    protected String name;
    @NotNull(message = "age of person cannot be null for person ")
    protected int age;
    @NotEmpty(message = "email of person cannot be null for person ")
    protected String email;
    @NotEmpty(message = "life intro of person cannot be null for person ")
    protected String lifeIntro;
}

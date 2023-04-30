package iam.mfa.grpc.api.rest.data.model;

import iam.mfa.grpc.api.data.GrpcPersonResponse;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:52
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class PersonDto extends BasePerson {
    @NotEmpty(message = "id cannot be null for person ")
    private String id;

    public GrpcPersonResponse toGrpc() {
        return GrpcPersonResponse.newBuilder()
                .setId(id)
                .setAge(age)
                .setName(name)
                .setEmail(email)
                .setLifeIntro(lifeIntro)
                .build();
    }
}

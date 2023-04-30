package iam.mfa.grpc.api.rest.data.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestPersonRetrieveRequest {
    @NotEmpty(message = "name of person cannot be null for person ")
    private String name;
    @NotEmpty(message = "email of person cannot be null for person ")
    private String email;
}

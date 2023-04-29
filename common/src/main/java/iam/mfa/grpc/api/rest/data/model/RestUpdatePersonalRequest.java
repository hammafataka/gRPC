package iam.mfa.grpc.api.rest.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestUpdatePersonalRequest {
    private String name;
    private int age;
    private String email;
    private String lifeInfo;
}

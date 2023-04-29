package iam.mfa.grpc.api.rest.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 2:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestPersonalInfo {
    private String id;
    private String name;
    private int age;
    private String email;
    private String lifeIntro;
}

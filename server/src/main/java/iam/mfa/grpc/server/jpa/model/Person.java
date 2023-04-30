package iam.mfa.grpc.server.jpa.model;

import java.util.UUID;

import iam.mfa.grpc.api.data.GrpcPersonRequest;
import iam.mfa.grpc.api.rest.data.model.BasePerson;
import iam.mfa.grpc.api.rest.data.model.PersonDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:28
 */
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    private UUID uuid;
    @Column
    private String name;
    @Column
    private int age;
    @Column
    private String email;
    @Column(name = "life_intro")
    private String lifeIntro;


    public PersonDto asDto() {
        return PersonDto.builder()
                .id(uuid.toString())
                .age(age)
                .name(name)
                .email(email)
                .lifeIntro(lifeIntro)
                .build();
    }

    public static Person fromGrpc(final GrpcPersonRequest request) {
        return Person.builder()
                .uuid(UUID.randomUUID())
                .name(request.getName())
                .age(request.getAge())
                .email(request.getEmail())
                .lifeIntro(request.getLifeIntro())
                .build();
    }

    public static Person fromRest(final BasePerson request) {
        return Person.builder()
                .uuid(UUID.randomUUID())
                .name(request.getName())
                .age(request.getAge())
                .email(request.getEmail())
                .lifeIntro(request.getLifeIntro())
                .build();
    }
}

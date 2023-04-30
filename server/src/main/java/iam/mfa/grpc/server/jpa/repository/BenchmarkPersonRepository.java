package iam.mfa.grpc.server.jpa.repository;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import iam.mfa.grpc.server.jpa.model.Person;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:36
 */

@Component
@Profile("benchmark")
public class BenchmarkPersonRepository implements PersonRepository {
    private final Person dummyPerson = Person.builder()
            .uuid(UUID.randomUUID())
            .age(23)
            .name("hamma")
            .email("m_fataka@utb.cz")
            .lifeIntro("some interesting life intro")
            .build();

    @Override
    public Person savePerson(Person person) {
        return dummyPerson;
    }

    @Override
    public Person findPersonByNameAndEmail(String name, String email) {
        return dummyPerson;
    }

    @Override
    public boolean personExistsByNameAndEmail(String name, String email) {
        return false;
    }
}

package iam.mfa.grpc.server.jpa.repository;

import java.util.UUID;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import iam.mfa.grpc.server.jpa.model.Person;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:32
 */
@Profile("!benchmark")
public interface PersonJpaRepository extends JpaRepository<Person, UUID>, PersonRepository {
    @Override
    default Person savePerson(final Person person) {
        return save(person);
    }

    Person findPersonByNameAndEmail(final String name, final String email);

    boolean existsByNameAndEmail(final String name, final String email);

    @Override
    default boolean personExistsByNameAndEmail(final String name, final String email) {
        return existsByNameAndEmail(name, email);
    }
}

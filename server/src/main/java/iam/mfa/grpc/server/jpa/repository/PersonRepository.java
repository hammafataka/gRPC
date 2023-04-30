package iam.mfa.grpc.server.jpa.repository;

import iam.mfa.grpc.server.jpa.model.Person;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project gRPC
 * @date 29.04.2023 22:28
 */
public interface PersonRepository {

    Person savePerson(final Person person);


    Person findPersonByNameAndEmail(final String name, final String email);
}

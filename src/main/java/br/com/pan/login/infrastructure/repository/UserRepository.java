package br.com.pan.login.infrastructure.repository;


import br.com.pan.login.authentication.domain.LegalPerson;
import br.com.pan.login.authentication.domain.RegularPerson;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRepository {

    private final MongoTemplate mongoTemplate;

    public UserRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public Optional<RegularPerson> findByCpf(String cpf) {
        var criteria = Criteria.where("cpf").is(cpf);

        return Optional.ofNullable(mongoTemplate.findOne(Query.query(criteria), RegularPerson.class));
    }

    public Optional<LegalPerson> findByCnpj(String cnpj) {
        var criteria = Criteria.where("cnpj").is(cnpj);

        return Optional.ofNullable(mongoTemplate.findOne(Query.query(criteria), LegalPerson.class));
    }

    public boolean personExists(String id) {
        var criteria = Criteria.where("cpf").is(id);

        return mongoTemplate.exists(Query.query(criteria), RegularPerson.class);
    }

    public boolean legalPersonExists(String id) {
        var criteria = Criteria.where("cnpj").is(id);

        return mongoTemplate.exists(Query.query(criteria), LegalPerson.class);
    }
}
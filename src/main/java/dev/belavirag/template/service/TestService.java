package dev.belavirag.template.service;

import dev.belavirag.template.domain.entity.TestEntity;
import dev.belavirag.template.repository.TestRepository;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class TestService {
    @Inject
    TestRepository repository;

    @WithTransaction
    public Uni<List<TestEntity>> findAll() {
        return repository.listAll();
    }

    @WithTransaction
    public Uni<TestEntity> createItem(String test) {
        TestEntity entity = TestEntity.builder().test(test).build();
        return repository.persist(entity);
    }
}

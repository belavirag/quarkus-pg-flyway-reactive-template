package dev.belavirag.template.repository;

import dev.belavirag.template.domain.entity.TestEntity;
import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class TestRepository implements PanacheRepository<TestEntity> {

}

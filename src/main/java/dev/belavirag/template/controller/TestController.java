package dev.belavirag.template.controller;

import dev.belavirag.template.domain.entity.TestEntity;
import dev.belavirag.template.service.TestService;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.Nonnull;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Path("/")
@Slf4j
public class TestController {

    @Inject
    TestService service;

    @GET
    @Path("/healthcheck")
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> healthcheck() {
        return Uni.createFrom().item(Thread.currentThread().getName());
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<TestEntity>> index() {
        return service.findAll();
    }

    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<TestEntity> create(@FormParam("text") @Nonnull String test){
        return service.createItem(test);
    }

}

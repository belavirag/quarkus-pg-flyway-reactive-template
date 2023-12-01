package dev.belavirag;

import io.quarkus.arc.Arc;
import io.quarkus.arc.InstanceHandle;
import io.quarkus.flyway.runtime.FlywayContainer;
import io.quarkus.flyway.runtime.FlywayContainerProducer;
import io.quarkus.flyway.runtime.QuarkusPathLocationScanner;
import io.quarkus.runtime.Startup;
import io.quarkus.scheduler.Scheduler;
import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

// https://github.com/quarkusio/quarkus/issues/10716
@Startup
public class RunFlyway {

    RunFlyway(Scheduler scheduler,
                    SessionFactory sessionFactory,
                    FlywayConfig flywayConfig,
                    @ConfigProperty(name = "quarkus.datasource.reactive.url") String datasourceUrl,
                    @ConfigProperty(name = "quarkus.datasource.username") String datasourceUsername,
                    @ConfigProperty(name = "quarkus.datasource.password") String datasourcePassword) {

        DataSource ds = Flyway.configure().dataSource("jdbc:" + datasourceUrl.substring("vertx-reactive:".length()),
                datasourceUsername,
                datasourcePassword).getDataSource();

        QuarkusPathLocationScanner.setApplicationMigrationFiles(flywayConfig
                .files()
                .stream()
                .map(file -> "db/migration/" + file)
                .collect(Collectors.toList()));

        try(InstanceHandle<FlywayContainerProducer> flywayContainerHandle = Arc.container().instance(FlywayContainerProducer.class)) {
            FlywayContainerProducer flywayProducer = flywayContainerHandle.get();
            FlywayContainer flywayContainer = flywayProducer.createFlyway(ds, "<default>", true, true);
            Flyway flyway = flywayContainer.getFlyway();

            if (flywayConfig.migrateAtStart()) {
                scheduler.pause();
                if (flywayConfig.cleanAtStart()) {
                    flyway.clean();
                }
                flyway.migrate();
                sessionFactory.getSchemaManager().validateMappedObjects();
                scheduler.resume();
            }
        }
    }

    @ConfigMapping(prefix = "application.database.flyway")
    public interface FlywayConfig {

        boolean migrateAtStart();

        @WithDefault("false")
        boolean cleanAtStart();

        List<String> files();
    }
}
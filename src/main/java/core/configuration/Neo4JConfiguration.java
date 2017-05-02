package core.configuration;

import org.neo4j.ogm.config.Configuration;
import org.neo4j.ogm.session.*;
import org.springframework.context.annotation.*;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@org.springframework.context.annotation.Configuration
@EnableNeo4jRepositories("core")
@EnableTransactionManagement
public class Neo4JConfiguration extends Neo4jConfiguration {

    @Bean
    public Configuration getConfiguration() {
        Configuration config = new Configuration();
        config.driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI("http://neo4j:root@localhost:7474");
        return config;
    }

    @Bean
    public SessionFactory getSessionFactory() {
        return new SessionFactory(getConfiguration(), "core");
    }

    @Bean
    @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
    public Session getSession() throws Exception {
        return super.getSession();
    }
}

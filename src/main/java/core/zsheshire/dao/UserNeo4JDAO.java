package core.zsheshire.dao;


import core.zsheshire.domain.User;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserNeo4JDAO extends GraphRepository<User> {

}

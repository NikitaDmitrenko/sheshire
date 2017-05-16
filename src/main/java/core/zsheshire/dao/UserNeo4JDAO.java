package core.zsheshire.dao;


import core.zsheshire.domain.User;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.GraphRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Repository
public interface UserNeo4JDAO extends GraphRepository<User> {


    @Transactional(isolation = Isolation.SERIALIZABLE)
    @Query("MATCH (user:User) with user MATCH (user1:User) WHERE user.MOBILE_PHONE={myId} AND user1.MOBILE_PHONE={anotherId} CREATE (user)-[r:FATHER]->(user1)")
    void subscribeUsers(@Param("myId") String mobilePhone, @Param("anotherId") String anotherUserMobilePhone);

    @Transactional(readOnly = true)
    @Query(value = "MATCH (user:User) with user MATCH (user1:User) WHERE user.MOBILE_PHONE={myId} and user1.MOBILE_PHONE={anotherId} OPTIONAL MATCH (user)-[r:FATHER]->(user1:User) RETURN case when count(r)>0 then true else false end")
    boolean checkForRelation(@Param("myId") String myMobilePhone, @Param("anotherId") String anotherMobilePhone);

    @Transactional
    @Query(value = "MATCH (user:User) with user MATCH (user1:User) WHERE user.MOBILE_PHONE={myMobilePhone} OPTIONAL MATCH (user)-[r:FATHER]->(user1:User) RETURN case when count(r)>0 then true else false end")
    boolean checkforRelation(@Param("myMobilePhone") String mobilePhone);

    @Transactional
    @Query(value = "MATCH (user:User {MOBILE_PHONE:{myPhone}})-[*1..3]-(hollywood) RETURN DISTINCT hollywood")
    List<User> getUsers(@Param("myPhone") String myPhone);

    @Transactional
    @Query("MATCH (user:User {MOBILE_PHONE:{myPhone}})-[r:FATHER]-(user1:User{MOBILE_PHONE:{anotherUserPhone}}) RETURN case when count(r)>0 then true else false end")
    boolean checkForParent(@Param("myPhone")String myPhone,@Param("anotherUserPhone")String anotherUserPhone);
}

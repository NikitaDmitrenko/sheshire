package core.zsheshire.dao;

import core.zsheshire.domain.Authorities;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.Session;

@Repository
public class AuthoritiesDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public void save(Authorities authorities) {
        sessionFactory.getCurrentSession().save(authorities);
    }
}

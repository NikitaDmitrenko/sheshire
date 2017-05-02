package core.zsheshire.dao;

import core.zsheshire.domain.User;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public class UserDAO {

    @Autowired
    public SessionFactory factory;

    @Transactional
    public User findByMobilePhone(String mobilePhone) {
        return (User) factory.getCurrentSession().createQuery("from User where mobilePhone=:mobilePhone").
                setParameter("mobilePhone", mobilePhone).
                uniqueResult();
    }

    @Transactional
    public void save(User user) {
        factory.getCurrentSession().save(user);
    }

    @Transactional
    public User findById(Long id) {
        return factory.getCurrentSession().get(User.class, id);
    }

    @Transactional
    public User findByIdWithMediaFiles(Long id) {
        User user = (User) factory.getCurrentSession().createQuery("from User user where user.id=:id").setParameter("id", id).uniqueResult();
        Hibernate.initialize(user.getMediaFiles());
        return user;
    }

    @Transactional
    public void update(User one) {
        factory.getCurrentSession().update(one);
    }
}

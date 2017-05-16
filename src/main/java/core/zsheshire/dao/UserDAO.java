package core.zsheshire.dao;

import core.zsheshire.domain.User;
import org.hibernate.Hibernate;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Repository
@Transactional
public class UserDAO {

    @Autowired
    public SessionFactory factory;

    @Transactional
    public User findByMobilePhone(String mobilePhone) {
        User user = (User) factory.getCurrentSession().createQuery("from User where mobilePhone=:mobilePhone and real_user=true").setParameter("mobilePhone", mobilePhone).uniqueResult();
        if (user != null) {
            Hibernate.initialize(user.getMediaFiles());
        }
        return user;
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
        if (user != null) {
            Hibernate.initialize(user.getMediaFiles());
        }
        return user;
    }

    @Transactional
    public void update(User one) {
        factory.getCurrentSession().update(one);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> findListOfUsers(Long step) {
        List<User> users = (List<User>) factory.getCurrentSession().createQuery("from User where real_user=true").setMaxResults(10).setFirstResult(step.intValue() * 10).list();
        if (!users.isEmpty()) {
            users.forEach(user1 -> Hibernate.initialize(user1.getMediaFiles()));
        }
        return users;
    }

    @Transactional
    public User findUserByUUID(String uuid) {
        return (User) factory.getCurrentSession().createQuery("from User where publicUUID=:publicUUID").setParameter("publicUUID", uuid).uniqueResult();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> findUsersBySurname(String surname, String name) {
        List<User> users = (List<User>) factory.getCurrentSession().
                createQuery("from User where real_user=true and surName like :surname and name like :name").
                setParameter("name", "'%'" + name + "'%'").
                setParameter("surname", "'%'" + surname + "'%'").
                list();
        if (!users.isEmpty()) {
            users.forEach(user1 -> Hibernate.initialize(user1.getMediaFiles()));
        }
        return users;
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<User> findUserByMobilePhone(List<String> collect) {
       List<User> users =  factory.getCurrentSession().createQuery("from User where mobilePhone in (:phones)").setParameterList("phones", collect).list();
        if (!users.isEmpty()) {
            users.forEach(user1 -> Hibernate.initialize(user1.getMediaFiles()));
        }
        return users;
    }
}

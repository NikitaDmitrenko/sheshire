package core.zsheshire.dao;

import core.zsheshire.domain.MediaFile;
import core.zsheshire.domain.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class MediaFileDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Transactional
    public void save(MediaFile mediaFile) {
        sessionFactory.getCurrentSession().save(mediaFile);
    }

    @Transactional
    @SuppressWarnings("unchecked")
    public List<MediaFile> getUserMediaFiles(User id) {
        return sessionFactory.getCurrentSession().createQuery("from MediaFile where user =:user").setParameter("user", id).list();
    }
}

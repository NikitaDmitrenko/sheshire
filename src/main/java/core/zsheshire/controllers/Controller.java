package core.zsheshire.controllers;

import core.zsheshire.dao.AuthoritiesDAO;
import core.zsheshire.dao.MediaFileDAO;
import core.zsheshire.dao.UserDAO;
import core.zsheshire.dao.UserNeo4JDAO;
import core.zsheshire.domain.Authorities;
import core.zsheshire.domain.MediaFile;
import core.zsheshire.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class Controller {

    @Autowired
    UserDAO userDAO;

    @Autowired
    UserNeo4JDAO userNeo4JDAO;

    @Autowired
    AuthoritiesDAO authoritiesDAO;
    @Autowired
    private MediaFileDAO mediaFileDAO;

    @PostMapping("/registration")
    public ResponseEntity<Map<String, Object>> registration(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus;

        User mobilePhone = userDAO.findByMobilePhone(user.getMobilePhone());

        if (mobilePhone == null) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userNeo4JDAO.save(user);
            userDAO.save(user);
            Authorities authorities = new Authorities();
            authorities.setUsername(user.getMobilePhone());
            authoritiesDAO.save(authorities);
            httpStatus = HttpStatus.ACCEPTED;
            response.put("registration", true);
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.put("registration", false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }


    @PutMapping("/usser/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateProfile(@PathVariable Long id, @RequestBody User user) throws Throwable {
        User one = userDAO.findById(id);
        Map<String, Object> response = new HashMap<>();
        if (one == null) {
            throw new Throwable();
        }
        one.setName(user.getName());
        one.setSurName(user.getSurName());
        one.setDateBirth(user.getDateBirth());
        one.setDateDeath(user.getDateDeath());
        one.setAddress(user.getAddress());
        one.setContacts(user.getContacts());
        if (!user.getMediaFiles().isEmpty()) {
            List<MediaFile> mediaFiles;
            if (user.getMediaFiles().size() > 50) {
                mediaFiles = user.getMediaFiles().subList(0, 50);
            } else {
                mediaFiles = user.getMediaFiles();
            }
            for (MediaFile mediaFile : mediaFiles) {
                mediaFile.setUser(one);
            }
            mediaFiles.forEach(mediaFile -> mediaFileDAO.save(mediaFile));
            one.setMediaFiles(mediaFiles);
            userDAO.update(one);


        }
        response.put("update", true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/usser/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Long id) throws Exception {
        Map<String, Object> response = new HashMap<>();
        User one = userDAO.findByIdWithMediaFiles(id);
        if (one == null) {
            throw new NullPointerException();
        }
        response.put("user", one);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

}

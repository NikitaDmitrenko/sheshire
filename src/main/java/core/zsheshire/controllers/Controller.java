package core.zsheshire.controllers;

import core.zsheshire.dao.*;
import core.zsheshire.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;

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
    @Transactional
    public ResponseEntity<Map<String, Object>> registration(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        HttpStatus httpStatus;

        User mobilePhone = userDAO.findByMobilePhone(user.getMobilePhone());

        if (mobilePhone == null) {
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            user.setReal_user(true);
            saveToNeo4j(user);
            saveToMySQL(user);
            saveAuth(user);
            httpStatus = HttpStatus.ACCEPTED;
            response.put("registration", true);
        } else {
            httpStatus = HttpStatus.BAD_REQUEST;
            response.put("registration", false);
        }
        return new ResponseEntity<>(response, httpStatus);
    }

    @Transactional
    public void saveToNeo4j(User user) {
        userNeo4JDAO.save(user);
    }


    @Transactional
    public void saveToMySQL(User user) {
        userDAO.save(user);
    }

    @Transactional
    public void saveAuth(User user) {
        Authorities authorities = new Authorities();
        authorities.setUsername(user.getMobilePhone());
        authoritiesDAO.save(authorities);
    }


    @PutMapping("/user/")
    @Transactional
    public ResponseEntity<Map<String, Object>> updateProfile(Principal principal, @RequestBody User user) throws Throwable {
        User one = userDAO.findByMobilePhone(principal.getName());
        Map<String, Object> response = new HashMap<>();
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

    @PutMapping("/user/media_file")
    public ResponseEntity<Map<String, Object>> addMediaFile(Principal principal, @RequestBody MediaFile mediaFile) {
        Map<String, Object> response = new HashMap<>();
        User user = userDAO.findByMobilePhone(principal.getName());
        List<MediaFile> mediaFiles = user.getMediaFiles();
        if (mediaFiles != null) {
            mediaFiles.add(mediaFile);
        }
        user.setMediaFiles(mediaFiles);
        userDAO.update(user);
        response.put("response", true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

    }


    @GetMapping("/user/{id}")
    @Transactional
    public ResponseEntity<Map<String, Object>> getUserInfo(@PathVariable Long id) throws Exception, ShezireException {
        Map<String, Object> response = new HashMap<>();
        User one = userDAO.findByIdWithMediaFiles(id);
        if (one == null) {
            throw new ShezireException("Can't find user with following id", "0");
        }
        response.put("user", one);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/profile")
    @Transactional
    public ResponseEntity<Map<String, Object>> getMyProfile(Principal principal) throws Exception {
        Map<String, Object> response = new HashMap<>();
        User one = userDAO.findByMobilePhone(principal.getName());
        response.put("user", one);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }


    @GetMapping("/user/{id:\\d+}/subscribe/{anotherId:\\d+}")
    @Transactional
    public ResponseEntity<Map<String, Object>> subscribeOnUser(@PathVariable Long id, Principal principal, @PathVariable Long anotherId) throws Exception, ShezireException {
        Map<String, Object> response = new HashMap<>();
        User one = userDAO.findByIdWithMediaFiles(id);
        User two = userDAO.findByIdWithMediaFiles(anotherId);

        if (one == null) {
            throw new ShezireException("Can't find user with following id", "0");
        }
        if (two != null && !userNeo4JDAO.checkforRelation(one.getMobilePhone()) && !userNeo4JDAO.checkForRelation(one.getMobilePhone(), two.getMobilePhone())) {
            userNeo4JDAO.subscribeUsers(one.getMobilePhone(), two.getMobilePhone());
            response.put("subscribe", true);
        } else {
            response.put("subscribe", false);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PutMapping("/user/{id:\\d+}/{anotherId:\\d+}")
    @Transactional
    public ResponseEntity<Map<String, Object>> editAnotherUser(@RequestBody User user, @PathVariable Long id, Principal principal, @PathVariable Long anotherId) {
        Map<String, Object> response = new HashMap<>();
        User one = userDAO.findByIdWithMediaFiles(id);
        User two = userDAO.findByIdWithMediaFiles(anotherId);
        if (two != null && userNeo4JDAO.checkForParent(one.getMobilePhone(), two.getMobilePhone())) {
            two.setName(user.getName());
            two.setSurName(user.getSurName());
            two.setDateBirth(user.getDateBirth());
            two.setDateDeath(user.getDateDeath());
            two.setAddress(user.getAddress());
            two.setContacts(user.getContacts());
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
                if (!two.getMediaFiles().isEmpty()) {
                    two.getMediaFiles().addAll(mediaFiles);
                } else {
                    two.setMediaFiles(mediaFiles);
                }
                userDAO.update(two);
            }
            response.put("update", true);
            response.put("subscribe", true);
        } else {
            response.put("subscribe", false);
        }
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/user/subscribe")
    @Transactional
    public ResponseEntity<Map<String, Object>> subscribeNewUser(@RequestParam(name = "id") String id, @RequestBody User user) throws Exception, ShezireException {
        Map<String, Object> response = new HashMap<>();
        String uuid = UUID.randomUUID().toString();
        User one = userDAO.findByIdWithMediaFiles(Long.valueOf(id));
        if (one == null) {
            throw new ShezireException("Can't find user with following id", "0");
        }
        user.setReal_user(false);
        user.setPublicUUID(uuid);
        if (user.getMediaFiles() != null && !user.getMediaFiles().isEmpty()) {
            List<MediaFile> mediaFiles = user.getMediaFiles();
            for (MediaFile mediaFile : mediaFiles) {
                mediaFileDAO.save(mediaFile);
            }
            user.setMediaFiles(mediaFiles);
        }
        user.setMobilePhone(uuid);
        saveToNeo4j(user);
        saveToMySQL(user);
        User userByUUID = userDAO.findUserByUUID(uuid);
        userNeo4JDAO.subscribeUsers(one.getMobilePhone(), userByUUID.getMobilePhone());
        response.put("subscribe", true);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/profiles/{step:\\d+}")
    public ResponseEntity getOtherProfiles(@PathVariable Long step) {
        Map<String, Object> response = new HashMap<>();
        List<User> one = userDAO.findListOfUsers(step);

        response.put("users", one);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @PostMapping("/user/profiles/{step:\\d+}")
    public ResponseEntity<Map<String, Object>> getProfilesBySurname(@PathVariable Long step, @RequestBody Keyword keyword) throws ShezireException {
        Map<String, Object> response = new HashMap<>();

        if (keyword == null) {
            throw new ShezireException("Keyword is null", "0");
        }
        String[] strings = keyword.getKeyWord().split(" ");
        if (strings.length != 2) {
            throw new ShezireException("Can't split by space", "1");
        }
        List<User> one = userDAO.findUsersBySurname(strings[0], strings[1]);
        response.put("users", one);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }

    @GetMapping("/user/tree")
    public ResponseEntity getTree(Principal principal) {
        Map<String, Object> response = new HashMap<>();
        List<User> users = userNeo4JDAO.getUsers(principal.getName());
        if (!users.isEmpty()) {
            List<String> collect = users.stream().map(User::getMobilePhone).collect(Collectors.toList());
            users = userDAO.findUserByMobilePhone(collect);
        }
        response.put("users", users);
        return new ResponseEntity<>(response, HttpStatus.ACCEPTED);
    }
}

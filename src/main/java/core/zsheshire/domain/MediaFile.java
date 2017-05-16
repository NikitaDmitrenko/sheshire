package core.zsheshire.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Data
@Entity
@Component
@Table(name = "MEDIA_FILE")
@JsonIgnoreProperties(value = "user")
public class MediaFile {
    @Id
    @GeneratedValue
    private int id;

    @Column(name = "SMALL_PHOTO")
    private String smallPhotoURL;

    @Column(name = "BIG_PHOTO")
    private String bigPhotoURL;

    @Column(name = "VIDEO")
    private String videoURL;

    @ManyToOne
    private User user;

    @Override
    public String toString() {
        return "MediaFile{" +
                "id=" + id +
                ", smallPhotoURL='" + smallPhotoURL + '\'' +
                ", bigPhotoURL='" + bigPhotoURL + '\'' +
                ", videoURL='" + videoURL + '\'' + user.getId()+
                '}';
    }
}

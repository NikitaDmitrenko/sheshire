package core.zsheshire.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "AUTHORITIES")
public class Authorities {

    @Id
    @Column(name = "USERNAME")
    private String username;

    @Column(name = "AUTHORITY")
    private String authority = "ROLE_USER";
}

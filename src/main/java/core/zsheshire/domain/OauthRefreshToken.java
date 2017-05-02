package core.zsheshire.domain;

import lombok.Data;

import javax.persistence.*;


@Entity
@Data
@Table(name = "oauth_refresh_token", schema = "shezire", catalog = "")
public class OauthRefreshToken {

    @Column(name = "token_id")
    @Id
    private String tokenId;

    @Column(name = "token")
    private byte[] token;

    @Column(name = "authentication")
    private byte[] authentication;

}

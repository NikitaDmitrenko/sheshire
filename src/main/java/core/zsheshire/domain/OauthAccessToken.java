package core.zsheshire.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "oauth_access_token", schema = "shezire", catalog = "")
public class OauthAccessToken {

    @Column(name = "token_id")
    private String tokenId;

    @Column(name = "token")
    private byte[] token;

    @Id
    @Column(name = "authentication_id")
    private String authenticationId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "authentication")
    private byte[] authentication;

    @Column(name = "refresh_token")
    private String refreshToken;

}

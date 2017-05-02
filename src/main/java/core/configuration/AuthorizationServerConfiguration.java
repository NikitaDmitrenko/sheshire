package core.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfiguration extends AuthorizationServerConfigurerAdapter {

    private static String REALM = "MY_OAUTH_REALM";

    private static final String ANDROID_CLIENT = "4imSOltMDrqZMo4KEk5RD82T0R7VE29fZV04z8Xxl0RoR9iWyJup6QKMzSiv3m3A7p3F7";
    private static final String ANDROID_KEY = "rrVjjnN0yFgl8P2RvZ7jRty8Rn69gMZlXyE4eaISPn0Y9zWiPU4lAw76cXq4gD2g4vZc47";
    private static final String IOS_CLIENT = "HZoe9RouNzf2P1Pk8zVG4tUenThL8FcJFEX5nMmQXtzcyGXlLBj7Jk0167nsVPTKVF8M2n";
    private static final String IOS_KEY = "pqQutAh5tqac6p0YohIrbqe3f3J75sOPoijYCjiF91NlQnGr9seu2AHMn4Yy8pXG2y6n0b";
    private static final String WEB_CLIENT = "IHa3pMK8F9q335oMnaALWGoTyK00XxOAqECmc2MaEVJ2SuCe9uy1Lr0IEeEbu1Or78Rltm";
    private static final String WEB_KEY = "sSatXV8tJaLgwmXZopoQ1p4Nf0EcAmiPSanSOsmnKfTx3cQyP9E6uiCFZM6nUTVqRie5jB";

    @Autowired
    private TokenStore tokenStore;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    DataSource dataSource;

    @Override
    @Transactional(transactionManager = "transactionManager")
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(ANDROID_CLIENT)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .authorities("ROLE_CLIENT","ROLE_USER", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write", "trust")
                .secret(ANDROID_KEY)
                .accessTokenValiditySeconds(1200000000).
                refreshTokenValiditySeconds(600).
                and().
                withClient(IOS_CLIENT)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .authorities("ROLE_CLIENT","ROLE_USER", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write", "trust")
                .secret(IOS_KEY)
                .accessTokenValiditySeconds(1200000000).
                refreshTokenValiditySeconds(600).
                and().
                withClient(WEB_CLIENT)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token", "implicit")
                .authorities("ROLE_CLIENT","ROLE_USER", "ROLE_TRUSTED_CLIENT")
                .scopes("read", "write", "trust")
                .secret(WEB_KEY)
                .accessTokenValiditySeconds(1200000000).
                refreshTokenValiditySeconds(600);
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore).userApprovalHandler(userApprovalHandler)
                .authenticationManager(authenticationManager);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer.realm(REALM + "/client");
    }

}
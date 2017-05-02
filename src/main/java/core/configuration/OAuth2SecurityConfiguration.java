package core.configuration;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.*;
import org.springframework.security.config.annotation.web.builders.*;
import org.springframework.security.config.annotation.web.configuration.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.*;
import org.springframework.security.oauth2.provider.approval.*;
import org.springframework.security.oauth2.provider.request.*;
import org.springframework.security.oauth2.provider.token.*;
import org.springframework.security.oauth2.provider.token.store.*;

import javax.sql.*;

@Configuration
@EnableWebSecurity
public class OAuth2SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ClientDetailsService clientDetailsService;

    private final DataSource dataSource;

    @Autowired
    public OAuth2SecurityConfiguration(ClientDetailsService clientDetailsService, DataSource dataSource) {
        this.clientDetailsService = clientDetailsService;
        this.dataSource = dataSource;
    }

    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().passwordEncoder(new BCryptPasswordEncoder()).
                dataSource(dataSource).
                usersByUsernameQuery("SELECT MOBILE_PHONE as USERNAME,PASSWORD,ENABLED FROM USER where MOBILE_PHONE=?").
                authoritiesByUsernameQuery("SELECT AUTHORITIES.USERNAME,AUTHORITIES.AUTHORITY FROM AUTHORITIES  WHERE AUTHORITIES.USERNAME=?");

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .anonymous().disable()
                .authorizeRequests()
                .antMatchers("/oauth/token").permitAll().
                and().
                formLogin().loginPage("/admin/login").loginProcessingUrl("/j_spring_security_check").
                defaultSuccessUrl("/admin/page?step=0").
                failureUrl("/error").
                usernameParameter("username").
                passwordParameter("password");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }

    @Bean
    @Autowired
    public TokenStoreUserApprovalHandler userApprovalHandler(TokenStore tokenStore) {
        TokenStoreUserApprovalHandler handler = new TokenStoreUserApprovalHandler();
        handler.setTokenStore(tokenStore);
        handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
        handler.setClientDetailsService(clientDetailsService);
        return handler;
    }

    @Bean
    @Autowired
    public ApprovalStore approvalStore(TokenStore tokenStore) throws Exception {
        TokenApprovalStore store = new TokenApprovalStore();
        store.setTokenStore(tokenStore);
        return store;
    }

}

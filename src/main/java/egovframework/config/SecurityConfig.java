package egovframework.config;

import egovframework.account.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity( prePostEnabled = true, proxyTargetClass = true )
public class SecurityConfig extends WebSecurityConfigurerAdapter {


    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/resources/**")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
            //접근권한
            .authorizeRequests()
                .antMatchers("/admin/**").hasRole("ADIM")
                .antMatchers("/user/**").hasRole("USER")
                .antMatchers("/board/create.do").hasAnyRole("USER","ADMIN")
                .anyRequest().permitAll()
            //로그인
            .and()
                .formLogin()
                .loginPage("/account/loginPage.do")
                .loginProcessingUrl("/login")
                .successHandler(new CustomSuccessHandler())
                .permitAll()

            //로그아웃
            .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/board/list.do")
                .invalidateHttpSession(true)
        ;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailService)
                .passwordEncoder(passwordEncoder());
    }

}



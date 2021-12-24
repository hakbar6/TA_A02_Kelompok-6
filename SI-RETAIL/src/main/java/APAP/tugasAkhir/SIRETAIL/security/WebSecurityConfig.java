package APAP.tugasAkhir.SIRETAIL.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception{
        http
                .authorizeRequests()
                .antMatchers("/css/**").permitAll()
                .antMatchers("/js/**").permitAll()
                .antMatchers("/api/**").permitAll()
                .antMatchers("/api-docs").permitAll()
                .antMatchers("/v3/api-docs/**").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/user/create").hasAuthority("Kepala Retail")
                .antMatchers("/cabang/create").hasAnyAuthority("Manager Cabang", "Kepala Retail")
                .antMatchers("/cabang/update/**").hasAnyAuthority("Manager Cabang", "Kepala Retail")
                .antMatchers("/cabang/delete/**").hasAnyAuthority("Manager Cabang", "Kepala Retail")
                .antMatchers("/cabang/addItem/**").hasAnyAuthority("Kepala Retail", "Manager Cabang")
                .antMatchers("/internal/api/cabang/accept/**").hasAnyAuthority("Kepala Retail")
                .antMatchers("/internal/api/cabang/reject/**").hasAnyAuthority("Kepala Retail")
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login").permitAll()
                .and()
                .exceptionHandling().accessDeniedPage("/error/403")
                .and()
                .cors()
                .and()
                .csrf()
                .disable();
    }

    @Bean
    public BCryptPasswordEncoder encoder(){ return new BCryptPasswordEncoder();}

//     buat testing
   @Autowired
   public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
       auth.inMemoryAuthentication()
               .passwordEncoder(encoder())
               .withUser("SIRETAIL").password(encoder().encode("SIRETAIL"))
               .roles("USER");
   }

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }
}


package egovframework.account.dto;

import egovframework.common.DefaultVO;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Account extends DefaultVO implements UserDetails{


    private String username;
    private String userNickname;
    private String password;
    private ArrayList<AccountRole> roles;

    public void roleSetter(String... role){
        this. roles = (ArrayList<AccountRole>)Arrays.stream(role).map(e->new AccountRole(e)).collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream().map(e -> new SimpleGrantedAuthority(e.getRole())).collect(Collectors.toList());
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}

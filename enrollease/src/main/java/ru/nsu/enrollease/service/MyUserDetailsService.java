package ru.nsu.enrollease.service;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.nsu.enrollease.model.ColleagueRole;
import ru.nsu.enrollease.model.RolePrivilege;
import ru.nsu.enrollease.repository.ColleagueRoleRepository;

@Service("userDetailsService")
@Transactional
@AllArgsConstructor
public class MyUserDetailsService implements UserDetailsService {

    private final ru.nsu.enrollease.repository.ColleagueRepository colleagueRepository;

    private final ColleagueService colleagueService;

    private final MessageSource messages;

    private final ColleagueRoleRepository colleagueRoleRepository;

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {

        var colleague = colleagueRepository.findByEmail(email);
        if (colleague.isEmpty()) {
            return new User(
                " ", " ", true, true, true, true,
                getAuthorities(List.of(
                    colleagueRoleRepository.findByName("DEFAULT_COLLEAGUE").orElseThrow())));
        }
        var user = colleague.get();
        return new User(
            user.getEmail(), " ", user.isEnabled(), true, true,
            true, getAuthorities(user.getRoles()));
    }

    private List<? extends GrantedAuthority> getAuthorities(
        List<ColleagueRole> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(List<ColleagueRole> roles) {

        List<String> privileges = new ArrayList<>();
        List<RolePrivilege> collection = new ArrayList<>();
        for (var role : roles) {
            privileges.add(role.getName());
            collection.addAll(role.getPrivileges());
        }
        for (RolePrivilege item : collection) {
            privileges.add(item.getName());
        }
        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }
}
package team.dankookie.server4983.member.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import team.dankookie.server4983.book.constant.Department;
import team.dankookie.server4983.common.domain.BaseEntity;
import team.dankookie.server4983.member.constant.AccountBank;
import team.dankookie.server4983.member.constant.UserRole;

import java.util.Collection;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Member extends BaseEntity implements UserDetails {

    @Id @GeneratedValue
    private Long id;

    @NotNull
    private String studentId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Department department;

    @NotNull
    private Integer yearOfAdmission;

    @NotNull
    @Column(unique = true)
    private String nickname;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String accountHolder;


    @NotNull
    @Enumerated(EnumType.STRING)
    private AccountBank accountBank;

    @NotNull
    private String accountNumber;

    private String imageUrl;

    @Column(columnDefinition = "boolean default false")
    private Boolean marketingAgree;

    @Builder
    public Member(String studentId, Department department, Integer yearOfAdmission, String nickname, String password, String phoneNumber, String accountHolder, AccountBank accountBank, String accountNumber, boolean marketingAgree){
        this.studentId = studentId;
        this.department = department;
        this.yearOfAdmission = yearOfAdmission;
        this.nickname = nickname;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.accountHolder = accountHolder;
        this.accountBank = accountBank;
        this.accountNumber = accountNumber;
        this.marketingAgree = marketingAgree;
        this.role = UserRole.USER;
    }

    public void changePassword(String password) {
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return nickname;
    }

    @Override
    public boolean isAccountNonExpired() {
        return !getDelYn();
    }

    @Override
    public boolean isAccountNonLocked() {
        return !getDelYn();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return !getDelYn();
    }

    @Override
    public boolean isEnabled() {
        return !getDelYn();
    }
}

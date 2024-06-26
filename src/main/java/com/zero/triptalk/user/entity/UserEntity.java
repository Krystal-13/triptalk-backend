package com.zero.triptalk.user.entity;

import com.zero.triptalk.user.enumType.LoginType;
import com.zero.triptalk.user.enumType.UserType;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "user")
public class UserEntity implements UserDetails {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long userId;

    private String name;

    private String profile;

    private String nickname;

    private String email;

    private String password;

    private String aboutMe;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime registerAt;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime updateAt;

    @Enumerated(EnumType.STRING)
    private UserType UserType;

    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    public static boolean isValidEmail(String email) {
        // 이메일 형식을 정규표현식으로 확인
        String emailRegex = "^[\\w-]+(\\.[\\w-]+)*@[\\w-]+(\\.[\\w-]+)*(\\.[a-zA-Z]{2,})$";
        return email.matches(emailRegex);
    }

    // 비밀번호 유효성 확인
    public static boolean isValidPassword(String password) {
        // 특수문자를 포함한 8글자 이상인지 확인
        return password.matches("^(?=.*[!@#$%^&*(),.?\":{}|<>])(?=.*[0-9])(?=.*[a-zA-Z]).{8,}$");
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(UserType.name()));
    }

    @Override
    public String getPassword(){
        return password;
    }

    @Override
    public String getUsername() {
        return email;
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

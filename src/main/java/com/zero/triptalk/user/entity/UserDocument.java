package com.zero.triptalk.user.entity;

import com.zero.triptalk.user.enumType.LoginType;
import com.zero.triptalk.user.enumType.UserType;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "user", writeTypeHint = WriteTypeHint.FALSE)
public class UserDocument {

    @Id
    private Long userId;
    private String profile;
    private String nickname;
    private String aboutMe;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime registerAt;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime updateAt;
    private UserType UserType;
    private LoginType loginType;

    @Builder
    public UserDocument(Long userId, String profile, String nickname, String aboutMe, LocalDateTime registerAt, LocalDateTime updateAt, com.zero.triptalk.user.enumType.UserType userType, LoginType loginType) {
        this.userId = userId;
        this.profile = profile;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
        this.registerAt = registerAt;
        this.updateAt = updateAt;
        this.UserType = userType;
        this.loginType = loginType;
    }

    public static UserDocument ofEntity(UserEntity user) {
        return UserDocument.builder()
                .userId(user.getUserId())
                .profile(user.getProfile())
                .nickname(user.getNickname())
                .aboutMe(user.getAboutMe())
                .registerAt(user.getRegisterAt())
                .updateAt(user.getUpdateAt())
                .userType(user.getUserType())
                .loginType(user.getLoginType())
                .build();
    }

}

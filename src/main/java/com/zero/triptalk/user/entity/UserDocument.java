package com.zero.triptalk.user.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.WriteTypeHint;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(indexName = "user", writeTypeHint = WriteTypeHint.FALSE)
public class UserDocument {

    @Id
    private Long userId;
    private String profile;
    private String nickname;
    private String aboutMe;

    @Builder
    public UserDocument(Long userId, String profile, String nickname, String aboutMe) {
        this.userId = userId;
        this.profile = profile;
        this.nickname = nickname;
        this.aboutMe = aboutMe;
    }
}

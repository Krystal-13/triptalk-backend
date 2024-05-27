package com.zero.triptalk.user.repository;

import com.zero.triptalk.user.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String username);

    Optional<UserEntity> findByNickname(String nickname);

    /**
     * mariaDB <-> elasticsearch 동기화 테스트에 필요한 데이터 조회 1.Users (Spring Scheduler 방식)
     */
    List<UserEntity> findAllByUpdateAtAfter(LocalDateTime updateAt);
}

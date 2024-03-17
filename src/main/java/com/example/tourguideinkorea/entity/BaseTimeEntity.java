package com.example.tourguideinkorea.entity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass // Entity가 이 클래스를 상속할 경우 이 클래스의 필드들도 칼럼으로 인식
@EntityListeners(AuditingEntityListener.class) // 시간측정 기능 추가
public abstract class BaseTimeEntity { // 인스턴스 생성 방지를 위해 추상클래스로 선언

    @CreatedDate // Entity 생성 시 시간 자동 저장
    private LocalDateTime createdDate;

    @LastModifiedDate // Entity 값 변경 시 시간 자동 저장
    private LocalDateTime modifiedDate;
}

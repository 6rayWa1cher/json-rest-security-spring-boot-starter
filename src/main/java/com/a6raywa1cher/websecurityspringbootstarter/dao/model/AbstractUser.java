package com.a6raywa1cher.websecurityspringbootstarter.dao.model;

import com.vladmihalcea.hibernate.type.json.JsonType;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@TypeDefs({
        @TypeDef(name = "json", typeClass = JsonType.class)
})
@Getter
@Setter
@ToString
@RequiredArgsConstructor
public abstract class AbstractUser implements IUser {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "user_role", nullable = false)
    private String userRole;

    @Column(name = "password")
    private String password;

    @Column(name = "refresh_tokens", columnDefinition = "jsonb")
    @Type(type = "json")
    private List<RefreshToken> refreshTokens;

    @Column(name = "last_visit_at")
    @CreatedDate
    private LocalDateTime lastVisitAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        AbstractUser that = (AbstractUser) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}

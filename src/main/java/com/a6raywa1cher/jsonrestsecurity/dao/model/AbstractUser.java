package com.a6raywa1cher.jsonrestsecurity.dao.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vladmihalcea.hibernate.type.json.JsonType;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Abstract implementation on {@link IUser}.
 * <br/>
 * Created to make creating custom implementations of IUser easier.
 * In most cases, the user just needs to extend this class and add custom fields.
 * <br/>
 *
 * @implNote Works best with PostgreSQL. In other databases, the user might have to
 * add a new domain {@code jsonb} and map it to either the json-compatible type or the string-compatible type
 * @see DefaultUser
 * @see JsonType
 */
@MappedSuperclass
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@TypeDefs({
	@TypeDef(name = "json", typeClass = JsonType.class)
})
public abstract class AbstractUser implements IUser {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id", nullable = false)
	private Long id;

	@Column(name = "username", nullable = false, unique = true)
	private String username;

	@Column(name = "user_role", nullable = false)
	private String userRole;

	@Column(name = "password")
	@JsonIgnore
	private String password;

	@Column(name = "refresh_tokens", columnDefinition = "jsonb")
	@Type(type = "json")
	@JsonIgnore
	private List<RefreshToken> refreshTokens;

	@Column(name = "last_visit_at")
	@CreatedDate
	private LocalDateTime lastVisitAt;

	public AbstractUser() {
	}

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

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getUserRole() {
		return this.userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<RefreshToken> getRefreshTokens() {
		return this.refreshTokens;
	}

	public void setRefreshTokens(List<RefreshToken> refreshTokens) {
		this.refreshTokens = refreshTokens;
	}

	public LocalDateTime getLastVisitAt() {
		return this.lastVisitAt;
	}

	public void setLastVisitAt(LocalDateTime lastVisitAt) {
		this.lastVisitAt = lastVisitAt;
	}

	public String toString() {
		return "AbstractUser(id=" + this.getId() + ", username=" + this.getUsername() + ", userRole=" + this.getUserRole() + ", password=" + this.getPassword() + ", refreshTokens=" + this.getRefreshTokens() + ", lastVisitAt=" + this.getLastVisitAt() + ")";
	}
}

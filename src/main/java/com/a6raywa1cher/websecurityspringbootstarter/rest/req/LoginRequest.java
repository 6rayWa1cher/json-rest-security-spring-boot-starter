package com.a6raywa1cher.websecurityspringbootstarter.rest.req;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class LoginRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 3, max = 128)
    private String password;

    public LoginRequest(@NotBlank @Email String email, @NotBlank @Size(min = 3, max = 128) String password) {
        this.email = email;
        this.password = password;
    }

    public LoginRequest() {
    }

    public @NotBlank @Email String getEmail() {
        return this.email;
    }

    public void setEmail(@NotBlank @Email String email) {
        this.email = email;
    }

    public @NotBlank @Size(min = 3, max = 128) String getPassword() {
        return this.password;
    }

    public void setPassword(@NotBlank @Size(min = 3, max = 128) String password) {
        this.password = password;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof LoginRequest)) return false;
        final LoginRequest other = (LoginRequest) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$email = this.getEmail();
        final Object other$email = other.getEmail();
        if (this$email == null ? other$email != null : !this$email.equals(other$email)) return false;
        final Object this$password = this.getPassword();
        final Object other$password = other.getPassword();
        if (this$password == null ? other$password != null : !this$password.equals(other$password)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof LoginRequest;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $email = this.getEmail();
        result = result * PRIME + ($email == null ? 43 : $email.hashCode());
        final Object $password = this.getPassword();
        result = result * PRIME + ($password == null ? 43 : $password.hashCode());
        return result;
    }

    public String toString() {
        return "LoginRequest(email=" + this.getEmail() + ", password=" + this.getPassword() + ")";
    }
}

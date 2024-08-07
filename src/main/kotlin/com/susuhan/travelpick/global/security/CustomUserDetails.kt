package com.susuhan.travelpick.global.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class CustomUserDetails(
    val userId: String,
    val role: String,
) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority?> {
        return Collections.singleton(SimpleGrantedAuthority(this.role))
    }

    fun getUserId() = this.userId.toLong()

    override fun getPassword(): String? {
        return null
    }

    override fun getUsername(): String? {
        return null
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }
}

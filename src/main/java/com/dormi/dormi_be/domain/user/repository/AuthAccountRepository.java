package com.dormi.dormi_be.domain.user.repository;

import com.dormi.dormi_be.domain.user.entity.AuthAccount;
import com.dormi.dormi_be.domain.user.enums.AuthProvider;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthAccountRepository extends JpaRepository<AuthAccount, Long> {

    boolean existsByProviderAndProviderUserId(AuthProvider provider, String providerUserId);

    Optional<AuthAccount> findByUser_IdAndProvider(Long userId, AuthProvider provider);

    @EntityGraph(attributePaths = "user")
    Optional<AuthAccount> findByProviderAndProviderUserId(AuthProvider provider, String providerUserId);
}

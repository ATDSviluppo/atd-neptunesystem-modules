package com.NeptuneWebAutheticator.Repository;

import com.NeptuneWebAutheticator.Entity.Tenant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, Long> {
    @Query("SELECT t FROM Tenant t WHERE t.username = :username AND t.password = :password")
    Tenant findByUsernameAndPassword(@Param("username") String username, @Param("password") String password);
}

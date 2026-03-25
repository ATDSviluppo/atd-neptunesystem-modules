package com.AuthenticationModule.Repository;

import com.CommonModule.CommonModule.Entity.EnumDeviceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumDeviceTypeRepository extends JpaRepository<EnumDeviceType, String> {
}

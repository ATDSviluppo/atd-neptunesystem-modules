package com.AuthenticationModule.Repository;

import com.CommonModule.CommonModule.Entity.EnumDeviceDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnumDeviceDetailRepository extends JpaRepository<EnumDeviceDetail, String> {
}

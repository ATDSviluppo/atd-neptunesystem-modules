package com.NeptuneWebAutheticator.Entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Machines")
public class Machine {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "machineId")
    private String machineId;

    @Column(name = "ipAddress")
    private String ipAddress;

    @Column(name = "isTruckingOn")
    private boolean isTruckingOn;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "tenantId",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_machine_tenant")
    )
    private Tenant tenant;
}

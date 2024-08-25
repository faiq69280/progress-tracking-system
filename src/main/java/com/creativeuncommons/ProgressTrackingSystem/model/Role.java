package com.creativeuncommons.ProgressTrackingSystem.model;

import com.creativeuncommons.ProgressTrackingSystem.query.ROLE_SCHEMA_DATA;

import java.util.Map;
import java.util.UUID;

public class Role {
    private UUID uuid;
    private String roleName;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Role(UUID uuid, String roleName) {
        this.uuid = uuid;
        this.roleName = roleName;
    }

    public static Role buildFromMap(Map<String, Object> row) {
        return new Role((UUID) row.get(ROLE_SCHEMA_DATA.ROLE_UUID.toString()),
                (String) row.get(ROLE_SCHEMA_DATA.ROLE_NAME.toString()));
    }
}

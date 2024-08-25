package com.creativeuncommons.ProgressTrackingSystem.query;

import com.creativeuncommons.ProgressTrackingSystem.querybuilder.InsertionBuilder;

import java.util.Map;
import java.util.UUID;

public class LinkUserRolesQueryProvider extends AbstractQueryProvider<LINK_USER_ROLE_SCHEMA_DATA>{
    public LinkUserRolesQueryProvider(){
        super(LINK_USER_ROLE_SCHEMA_DATA.class,"link_user_roles");
    }
    public InsertionBuilder<LINK_USER_ROLE_SCHEMA_DATA> setUserRolesQuery(UUID userID,
                                                                          UUID roleID){
        return getSQL_INSERT().setParamValues(
                Map.of(LINK_USER_ROLE_SCHEMA_DATA.USER_UUID,userID,
                        LINK_USER_ROLE_SCHEMA_DATA.ROLE_UUID,roleID)
        );

    }

}

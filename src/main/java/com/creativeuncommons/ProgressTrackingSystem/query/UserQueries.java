package com.creativeuncommons.ProgressTrackingSystem.query;

import com.creativeuncommons.ProgressTrackingSystem.model.Role;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.InsertionBuilder;
import com.creativeuncommons.ProgressTrackingSystem.querybuilder.SQLBuilder;
import org.springframework.data.util.Pair;

import java.util.*;

public class UserQueries extends AbstractQueryProvider<USER_SCHEMA_DATA> {
    public UserQueries(){
        super(USER_SCHEMA_DATA.class,"users");
    }

    public SQLBuilder getUserRolesQuery(UUID id){
        return new SQLBuilder().select(List.of("roles.*"), false)
                .from("link_user_roles")
                .join("link_user_roles",
                        "roles",
                        "role_uuid",
                        "role_uuid")
                .where(new WhereFiltersBuilder<>("link_user_roles.user_uuid", OPERATOR.EQUALS,
                        Pair.of("user_uuid", id)));
    }


}

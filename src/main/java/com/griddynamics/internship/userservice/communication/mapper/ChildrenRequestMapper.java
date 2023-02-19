package com.griddynamics.internship.userservice.communication.mapper;

import com.griddynamics.internship.userservice.communication.request.ChildrenDataRequest;
import com.griddynamics.internship.userservice.model.child.ChildAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ChildrenRequestMapper {
    ChildAccount requestToChild(ChildrenDataRequest childrenRequest);
}

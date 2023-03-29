package com.griddynamics.internship.userservice.component;

import com.griddynamics.internship.userservice.exception.NonExistentDataException;
import com.griddynamics.internship.userservice.model.user.UserWrapper;
import com.griddynamics.internship.userservice.repo.ChildrenRepository;
import com.griddynamics.internship.userservice.service.ChildrenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("accessCheck")
public class AccessCheckHelper {
    @Autowired
    private ChildrenRepository childrenRepository;

    public boolean hasChildAccountAccess(UserWrapper user, int childId) {
        if (user == null) return false;
        boolean isParent = childrenRepository
                .existsByIdAndParentsContaining(childId, user.getUser());
        return isParent;
    }
}

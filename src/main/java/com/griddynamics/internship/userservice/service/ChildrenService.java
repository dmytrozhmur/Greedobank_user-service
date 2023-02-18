package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.ChildrenResponseMapper;
import com.griddynamics.internship.userservice.communication.response.ChildrenPage;
import com.griddynamics.internship.userservice.exception.EmptyDataException;
import com.griddynamics.internship.userservice.model.child.ChildAccount;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.repo.ChildrenRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.griddynamics.internship.userservice.utils.PageRequests.generatePageRequest;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.EMPTY_RESPONSE;

@Service
public class ChildrenService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChildrenRepository childrenRepository;
    @Autowired
    private ChildrenResponseMapper responseMapper;

    public ChildrenPage findChildren(int[] userIds, int page, int size) {
        PageRequest pageRequest = generatePageRequest(page, size);
        List<User> pickedUsers = Arrays.stream(userIds)
                .mapToObj(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
        Page<ChildAccount> childrenAccounts = childrenRepository
                .findAllByParentsIn(pageRequest, pickedUsers);

        if(childrenAccounts.isEmpty()) throw new EmptyDataException(EMPTY_RESPONSE);
        return new ChildrenPage(
            responseMapper.childrenToDTO(childrenAccounts.getContent()),
                pageRequest,
                childrenAccounts.getTotalElements()
        );
    }
}

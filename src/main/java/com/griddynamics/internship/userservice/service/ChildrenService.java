package com.griddynamics.internship.userservice.service;

import com.griddynamics.internship.userservice.communication.mapper.ChildrenRequestMapper;
import com.griddynamics.internship.userservice.communication.mapper.ChildrenResponseMapper;
import com.griddynamics.internship.userservice.communication.request.ChildrenDataRequest;
import com.griddynamics.internship.userservice.communication.response.ChildrenPage;
import com.griddynamics.internship.userservice.exception.*;
import com.griddynamics.internship.userservice.model.child.ChildAccount;
import com.griddynamics.internship.userservice.model.user.User;
import com.griddynamics.internship.userservice.repo.ChildrenRepository;
import com.griddynamics.internship.userservice.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.validation.ConstraintViolationException;
import java.util.*;

import static com.griddynamics.internship.userservice.utils.PageRequests.generatePageRequest;
import static com.griddynamics.internship.userservice.utils.ResponseMessages.*;

@Service
public class ChildrenService {
    private static final String UNIQUE_IDENTIFIER = "login";
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChildrenRepository childrenRepository;
    @Autowired
    private ChildrenResponseMapper responseMapper;
    @Autowired
    private ChildrenRequestMapper requestMapper;

    public ChildrenPage findChildren(int[] userIds, int page, int size) {
        checkIds(userIds);
        PageRequest pageRequest = generatePageRequest(page, size);
        List<User> pickedUsers = Arrays.stream(userIds)
                .mapToObj(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
        List<ChildAccount> childrenAccounts = CollectionUtils.isEmpty(pickedUsers)
                ? childrenRepository.findAll()
                : childrenRepository.findAllByParentsIn(pageRequest, pickedUsers);
        return new ChildrenPage(
                responseMapper.childrenToDTO(childrenAccounts),
                pageRequest,
                childrenAccounts.size()
        );
    }

    public void createChild(int id, ChildrenDataRequest childrenRequest) {
        checkLogin(childrenRequest.getLogin());
        User parent = userRepository.findById(id)
                .orElseThrow(() -> new NonExistentDataException(USER_NOT_FOUND));
        ChildAccount childAccount = requestMapper.requestToChild(childrenRequest);
        childAccount.setParents(Collections.singletonList(parent));
        childrenRepository.save(childAccount);
    }

    private void checkLogin(String login) {
        if(childrenRepository.existsByLogin(login)) {
            Set<UniqueConstraintViolation> violation = Collections
                    .singleton(new UniqueConstraintViolation(UNIQUE_IDENTIFIER, NOT_UNIQUE));
            throw new ConstraintViolationException(violation);
        }
    }

    private void checkIds(int[] userIds) {
        boolean notExistingUsers = !userRepository.existsByIdIn(Arrays.stream(userIds)
                .boxed()
                .toList());
        if(userIds.length > 0 && notExistingUsers) throw new NonExistentDataException(USER_NOT_FOUND);
    }
}

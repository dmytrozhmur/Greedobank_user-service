package com.griddynamics.internship.userservice.communication.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.griddynamics.internship.userservice.model.user.UserDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class UserPage extends PageImpl<UserDTO> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public UserPage(@JsonProperty("content") List<UserDTO> content,
                    @JsonProperty("number") int number,
                    @JsonProperty("size") int size,
                    @JsonProperty("totalElements") Long totalElements,
                    @JsonProperty("pageable") JsonNode pageable,
                    @JsonProperty("last") boolean last,
                    @JsonProperty("totalPages") int totalPages,
                    @JsonProperty("sort") JsonNode sort,
                    @JsonProperty("first") boolean first,
                    @JsonProperty("numberOfElements") int numberOfElements,
                    @JsonProperty("empty") boolean empty) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public UserPage(List<UserDTO> content, Pageable pageRequest, long total) {
        super(content, pageRequest, total);
    }

    public UserPage(List<UserDTO> content) {
        super(content);
    }

    public UserPage() {
        super(new ArrayList<>());
    }
}

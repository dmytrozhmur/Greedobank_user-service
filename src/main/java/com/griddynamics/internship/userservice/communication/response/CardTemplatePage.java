package com.griddynamics.internship.userservice.communication.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import com.griddynamics.internship.userservice.model.card.CardTemplateDTO;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

public class CardTemplatePage extends PageImpl<CardTemplateDTO> {
    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CardTemplatePage(@JsonProperty("content") List<CardTemplateDTO> content,
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
        super(content, PageRequest.of(number, size < 1 ? 1 : size), totalElements);
    }

    public CardTemplatePage(List<CardTemplateDTO> content, Pageable pageRequest, long total) {
        super(content, pageRequest, total);
    }

    public CardTemplatePage(List<CardTemplateDTO> content) {
        super(content);
    }

    public CardTemplatePage() {
        super(new ArrayList<>());
    }
}

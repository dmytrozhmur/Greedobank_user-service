package com.griddynamics.internship.userservice.model.card;

import java.time.OffsetDateTime;

public record CardTemplateDTO(
        int id,
        String type,
        TariffDTO tariff,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        int createdById
) {
}

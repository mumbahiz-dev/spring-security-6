package com.mumbahiz.model;

public record LoginResponseDTO(
        String status,
        String jwtToken
) {
}

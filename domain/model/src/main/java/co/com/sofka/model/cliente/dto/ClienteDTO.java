package co.com.sofka.model.cliente.dto;

import lombok.Builder;

@Builder
public record ClienteDTO(String nit, String razonSocial) {
}

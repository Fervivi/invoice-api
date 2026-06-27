/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Detalle de error retornado por la API")
public class DtoApiError {
    @Schema(description = "Fecha del error", example = "2026-06-16")
    private LocalDate timestamp;

    @Schema(description = "Codigo HTTP", example = "404")
    private int status;

    @Schema(description = "Descripcion HTTP del error", example = "Not Found")
    private String error;

    @Schema(description = "Mensaje detallado del error", example = "Factura no encontrada con folio: 1")
    private String message;

    @Schema(description = "Ruta solicitada", example = "/api/v1/invoices/1")
    private String path;

    @Schema(description = "Clase de excepcion capturada", example = "ResourceNotFoundException")
    private String claseException;
}

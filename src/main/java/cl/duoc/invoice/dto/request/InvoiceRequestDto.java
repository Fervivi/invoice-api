/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Datos necesarios para crear una factura")
public class InvoiceRequestDto {
    @Schema(description = "Identificador de la venta asociada", example = "1001")
    @NotNull(message = "La venta asociada es obligatoria")
    private Long saleId;

    @Schema(description = "Fecha de emision de la factura", example = "2026-06-16")
    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @Schema(description = "Razon social del receptor", example = "Comercial Los Andes SpA")
    @NotBlank(message = "La razon social del receptor es obligatoria")
    private String razonSocialReceptor;

    @Schema(description = "Giro comercial del receptor", example = "Venta de articulos electronicos")
    @NotBlank(message = "El giro del receptor es obligatorio")
    private String giroReceptor;

    @Schema(description = "Direccion del receptor", example = "Av. Providencia 1234, Santiago")
    @NotBlank(message = "La direccion del receptor es obligatoria")
    private String direccionReceptor;

    @Schema(description = "RUT del receptor", example = "76.123.456-7")
    @NotBlank(message = "El RUT del receptor es obligatorio")
    private String rutReceptor;

    @Schema(description = "Razon social del emisor", example = "DuocUC FullStack 1")
    @NotBlank(message = "La razon social del emisor es obligatoria")
    private String razonSocialEmisor;

    @Schema(description = "Giro comercial del emisor", example = "Servicios tecnologicos")
    @NotBlank(message = "El giro del emisor es obligatorio")
    private String giroEmisor;

    @Schema(description = "Direccion del emisor", example = "Av. Siempre Viva 742, Santiago")
    @NotBlank(message = "La direccion del emisor es obligatoria")
    private String direccionEmisor;

    @Schema(description = "RUT del emisor", example = "77.987.654-3")
    @NotBlank(message = "El RUT del emisor es obligatorio")
    private String rutEmisor;

    @Schema(description = "Monto neto de la factura", example = "100000")
    @NotNull(message = "El monto neto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto neto debe ser mayor a 0")
    private BigDecimal montoNeto;
}

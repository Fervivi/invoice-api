/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.dto.request;

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
public class InvoiceRequestDto {
    @NotNull(message = "La venta asociada es obligatoria")
    private Long saleId;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fecha;

    @NotBlank(message = "La razón social del receptor es obligatoria")
    private String razonSocialReceptor;

    @NotBlank(message = "El giro del receptor es obligatorio")
    private String giroReceptor;

    @NotBlank(message = "La dirección del receptor es obligatoria")
    private String direccionReceptor;

    @NotBlank(message = "El RUT del receptor es obligatorio")
    private String rutReceptor;

    @NotBlank(message = "La razón social del emisor es obligatoria")
    private String razonSocialEmisor;

    @NotBlank(message = "El giro del emisor es obligatorio")
    private String giroEmisor;

    @NotBlank(message = "La dirección del emisor es obligatoria")
    private String direccionEmisor;

    @NotBlank(message = "El RUT del emisor es obligatorio")
    private String rutEmisor;

    @NotNull(message = "El monto neto es obligatorio")
    @DecimalMin(value = "0.01", message = "El monto neto debe ser mayor a 0")
    private BigDecimal montoNeto;
}

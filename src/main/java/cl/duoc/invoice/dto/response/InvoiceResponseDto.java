/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Factura registrada en el sistema")
public class InvoiceResponseDto {

    @Schema(description = "Identificador interno de la factura", example = "1")
    private Long id;

    @Schema(description = "Identificador de la venta asociada", example = "1001")
    private Long saleId;

    @Schema(description = "Folio tributario de la factura", example = "1")
    private Long folio;

    @Schema(description = "Fecha de emision de la factura", example = "2026-06-16")
    private LocalDate fecha;

    @Schema(description = "Razon social del receptor", example = "Comercial Los Andes SpA")
    private String razonSocialReceptor;

    @Schema(description = "Giro comercial del receptor", example = "Venta de articulos electronicos")
    private String giroReceptor;

    @Schema(description = "Direccion del receptor", example = "Av. Providencia 1234, Santiago")
    private String direccionReceptor;

    @Schema(description = "RUT del receptor", example = "76.123.456-7")
    private String rutReceptor;

    @Schema(description = "Razon social del emisor", example = "DuocUC FullStack 1")
    private String razonSocialEmisor;

    @Schema(description = "Giro comercial del emisor", example = "Servicios tecnologicos")
    private String giroEmisor;

    @Schema(description = "Direccion del emisor", example = "Av. Siempre Viva 742, Santiago")
    private String direccionEmisor;

    @Schema(description = "RUT del emisor", example = "77.987.654-3")
    private String rutEmisor;

    @Schema(description = "Monto neto de la factura", example = "100000")
    private BigDecimal montoNeto;

    @Schema(description = "IVA calculado de la factura", example = "19000")
    private BigDecimal iva;

    @Schema(description = "Monto total de la factura", example = "119000")
    private BigDecimal montoTotal;

    @Schema(description = "Indica si la factura fue anulada", example = "false")
    private Boolean anulada;
}

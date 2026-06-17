/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.controller;

import cl.duoc.invoice.dto.request.InvoiceRequestDto;
import cl.duoc.invoice.dto.response.InvoiceResponseDto;
import cl.duoc.invoice.service.InvoiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
@Tag(name = "Facturas", description = "Operaciones para crear, consultar y anular facturas")
@SecurityRequirement(name = "bearerAuth")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @GetMapping
    @Operation(summary = "Listar facturas", description = "Obtiene todas las facturas registradas.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Facturas obtenidas correctamente",
                content = @Content(array = @ArraySchema(schema = @Schema(implementation = InvoiceResponseDto.class)))),
        @ApiResponse(responseCode = "401", description = "Token JWT ausente o invalido", content = @Content)
    })
    public ResponseEntity<List<InvoiceResponseDto>> getInvoices() {
        List<InvoiceResponseDto> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(invoices);
    }

    @PostMapping
    @Operation(summary = "Crear factura", description = "Crea una factura y calcula IVA y monto total.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "201",
                description = "Factura creada correctamente",
                content = @Content(schema = @Schema(implementation = InvoiceResponseDto.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada invalidos", content = @Content),
        @ApiResponse(responseCode = "401", description = "Token JWT ausente o invalido", content = @Content)
    })
    public ResponseEntity<InvoiceResponseDto> createInvoice(@Valid @RequestBody InvoiceRequestDto request) {
        InvoiceResponseDto response = invoiceService.createInvoice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{folio}")
    @Operation(summary = "Buscar factura por folio", description = "Obtiene una factura usando su folio tributario.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Factura encontrada",
                content = @Content(schema = @Schema(implementation = InvoiceResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Token JWT ausente o invalido", content = @Content),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada", content = @Content)
    })
    public ResponseEntity<InvoiceResponseDto> getInvoiceByFolio(
            @Parameter(description = "Folio de la factura", example = "1") @PathVariable Long folio) {
        InvoiceResponseDto response = invoiceService.getInvoiceByFolio(folio);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{folio}/anular")
    @Operation(summary = "Anular factura", description = "Marca una factura existente como anulada.")
    @ApiResponses({
        @ApiResponse(
                responseCode = "200",
                description = "Factura anulada correctamente",
                content = @Content(schema = @Schema(implementation = InvoiceResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "Token JWT ausente o invalido", content = @Content),
        @ApiResponse(responseCode = "404", description = "Factura no encontrada", content = @Content),
        @ApiResponse(responseCode = "409", description = "La factura ya se encuentra anulada", content = @Content)
    })
    public ResponseEntity<InvoiceResponseDto> deleteInvoice(
            @Parameter(description = "Folio de la factura", example = "1") @PathVariable Long folio) {
        InvoiceResponseDto response = invoiceService.deleteInvoice(folio);
        return ResponseEntity.ok(response);
    }
}

/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "invoices")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long saleId;

    @Column(nullable = false)
    private LocalDate fecha;

    @Column(nullable = false, unique = true)
    private Long folio;

    @Column(nullable = false)
    private String razonSocialReceptor;

    @Column(nullable = false)
    private String giroReceptor;

    @Column(nullable = false)
    private String direccionReceptor;

    @Column(nullable = false)
    private String rutReceptor;

    @Column(nullable = false)
    private String razonSocialEmisor;

    @Column(nullable = false)
    private String giroEmisor;

    @Column(nullable = false)
    private String direccionEmisor;

    @Column(nullable = false)
    private String rutEmisor;

    @Column(nullable = false)
    private BigDecimal montoNeto;

    @Column(nullable = false)
    private BigDecimal iva;

    @Column(nullable = false)
    private BigDecimal montoTotal;

    @Column(nullable = false)
    private Boolean anulada = false;
}

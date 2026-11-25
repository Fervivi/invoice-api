/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.exception;

/** Se lanza al intentar anular una factura que ya fue anulada previamente. */
public class InvoiceAlreadyCancelledException extends RuntimeException {
    public InvoiceAlreadyCancelledException(Long folio) {
        super("La factura con folio " + folio + " ya se encuentra anulada");
    }
}

/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cl.duoc.invoice.dto.response.InvoiceResponseDto;
import cl.duoc.invoice.exception.InvoiceAlreadyCancelledException;
import cl.duoc.invoice.exception.ResourceNotFoundException;
import cl.duoc.invoice.model.InvoiceModel;
import cl.duoc.invoice.repository.InvoiceRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class InvoiceServiceTest {

    @Mock
    private InvoiceRepository invoiceRepository;

    @InjectMocks
    private InvoiceService invoiceService;

    private static InvoiceModel invoice(Long folio, boolean anulada) {
        InvoiceModel invoice = new InvoiceModel();
        invoice.setId(folio);
        invoice.setFolio(folio);
        invoice.setSaleId(10L);
        invoice.setFecha(LocalDate.of(2026, 11, 25));
        invoice.setMontoNeto(new BigDecimal("10000"));
        invoice.setIva(new BigDecimal("1900"));
        invoice.setMontoTotal(new BigDecimal("11900"));
        invoice.setAnulada(anulada);
        return invoice;
    }

    @Test
    void getInvoiceByFolio_existing_returnsInvoice() {
        when(invoiceRepository.findByFolio(1L)).thenReturn(Optional.of(invoice(1L, false)));

        InvoiceResponseDto response = invoiceService.getInvoiceByFolio(1L);

        assertThat(response.getFolio()).isEqualTo(1L);
        assertThat(response.getSaleId()).isEqualTo(10L);
    }

    @Test
    void getInvoiceByFolio_missing_throwsNotFound() {
        when(invoiceRepository.findByFolio(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.getInvoiceByFolio(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    @Test
    void anularInvoice_active_marksAsCancelled() {
        when(invoiceRepository.findByFolio(1L)).thenReturn(Optional.of(invoice(1L, false)));
        when(invoiceRepository.save(any(InvoiceModel.class))).thenAnswer(inv -> inv.getArgument(0));

        InvoiceResponseDto response = invoiceService.anularInvoice(1L);

        assertThat(response.getAnulada()).isTrue();
    }

    @Test
    void anularInvoice_alreadyCancelled_throwsConflictAndDoesNotSave() {
        when(invoiceRepository.findByFolio(1L)).thenReturn(Optional.of(invoice(1L, true)));

        assertThatThrownBy(() -> invoiceService.anularInvoice(1L))
                .isInstanceOf(InvoiceAlreadyCancelledException.class)
                .hasMessageContaining("1");
        verify(invoiceRepository, never()).save(any());
    }

    @Test
    void anularInvoice_missing_throwsNotFound() {
        when(invoiceRepository.findByFolio(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> invoiceService.anularInvoice(99L)).isInstanceOf(ResourceNotFoundException.class);
    }
}

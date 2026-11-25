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

import cl.duoc.invoice.dto.request.InvoiceRequestDto;
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

    private static InvoiceRequestDto request(String montoNeto) {
        InvoiceRequestDto request = new InvoiceRequestDto();
        request.setSaleId(10L);
        request.setFecha(LocalDate.of(2026, 11, 25));
        request.setRazonSocialReceptor("Cliente SPA");
        request.setGiroReceptor("Retail");
        request.setDireccionReceptor("Av. Siempre Viva 123");
        request.setRutReceptor("11.111.111-1");
        request.setRazonSocialEmisor("jKiltro SPA");
        request.setGiroEmisor("Venta de productos para mascotas");
        request.setDireccionEmisor("Av. Principal 456");
        request.setRutEmisor("76.543.210-K");
        request.setMontoNeto(new BigDecimal(montoNeto));
        return request;
    }

    @Test
    void createInvoice_calculatesIvaAndTotal() {
        when(invoiceRepository.findTopByOrderByFolioDesc()).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(InvoiceModel.class))).thenAnswer(inv -> inv.getArgument(0));

        InvoiceResponseDto response = invoiceService.createInvoice(request("10000"));

        assertThat(response.getMontoNeto()).isEqualByComparingTo("10000");
        assertThat(response.getIva()).isEqualByComparingTo("1900");
        assertThat(response.getMontoTotal()).isEqualByComparingTo("11900");
        assertThat(response.getAnulada()).isFalse();
    }

    @Test
    void createInvoice_roundsAmountsToWholePesos() {
        when(invoiceRepository.findTopByOrderByFolioDesc()).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(InvoiceModel.class))).thenAnswer(inv -> inv.getArgument(0));

        // 12345 * 0,19 = 2345,55 -> 2346
        InvoiceResponseDto response = invoiceService.createInvoice(request("12345"));

        assertThat(response.getIva()).isEqualByComparingTo("2346");
        assertThat(response.getIva().scale()).isZero();
        assertThat(response.getMontoTotal()).isEqualByComparingTo("14691");
    }

    @Test
    void createInvoice_firstInvoice_getsFolioOne() {
        when(invoiceRepository.findTopByOrderByFolioDesc()).thenReturn(Optional.empty());
        when(invoiceRepository.save(any(InvoiceModel.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(invoiceService.createInvoice(request("1000")).getFolio()).isEqualTo(1L);
    }

    @Test
    void createInvoice_nextInvoice_getsLastFolioPlusOne() {
        when(invoiceRepository.findTopByOrderByFolioDesc()).thenReturn(Optional.of(invoice(7L, false)));
        when(invoiceRepository.save(any(InvoiceModel.class))).thenAnswer(inv -> inv.getArgument(0));

        assertThat(invoiceService.createInvoice(request("1000")).getFolio()).isEqualTo(8L);
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

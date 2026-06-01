/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.service;

import cl.duoc.invoice.dto.request.InvoiceRequestDto;
import cl.duoc.invoice.dto.response.InvoiceResponseDto;
import cl.duoc.invoice.exception.ResourceNotFoundException;
import cl.duoc.invoice.model.InvoiceModel;
import cl.duoc.invoice.repository.InvoiceRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InvoiceService {

    private final InvoiceRepository invoiceRepository;

    public InvoiceResponseDto createInvoice(InvoiceRequestDto request) {

        Long nextFolio = getNextFolio();

        InvoiceModel invoice = new InvoiceModel();

        invoice.setSaleId(request.getSaleId());
        invoice.setFolio(nextFolio);
        invoice.setFecha(request.getFecha());

        invoice.setRazonSocialReceptor(request.getRazonSocialReceptor());
        invoice.setGiroReceptor(request.getGiroReceptor());
        invoice.setDireccionReceptor(request.getDireccionReceptor());
        invoice.setRutReceptor(request.getRutReceptor());

        invoice.setRazonSocialEmisor(request.getRazonSocialEmisor());
        invoice.setGiroEmisor(request.getGiroEmisor());
        invoice.setDireccionEmisor(request.getDireccionEmisor());
        invoice.setRutEmisor(request.getRutEmisor());

        BigDecimal montoNeto = request.getMontoNeto();
        BigDecimal iva = montoNeto.multiply(new BigDecimal("0.19"));
        BigDecimal montoTotal = montoNeto.add(iva);

        invoice.setMontoNeto(montoNeto);
        invoice.setIva(iva);
        invoice.setMontoTotal(montoTotal);
        invoice.setAnulada(false);

        InvoiceModel savedInvoice = invoiceRepository.save(invoice);

        return mapToResponseDto(savedInvoice);
    }

    public List<InvoiceResponseDto> getAllInvoices() {
        return invoiceRepository.findAll().stream().map(this::mapToResponseDto).collect(Collectors.toList());
    }

    public InvoiceResponseDto getInvoiceByFolio(Long folio) {
        InvoiceModel invoice = invoiceRepository
                .findByFolio(folio)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con folio: " + folio));

        return mapToResponseDto(invoice);
    }

    public InvoiceResponseDto deleteInvoice(Long folio) {
        InvoiceModel invoice = invoiceRepository
                .findByFolio(folio)
                .orElseThrow(() -> new ResourceNotFoundException("Factura no encontrada con folio: " + folio));

        if (Boolean.TRUE.equals(invoice.getAnulada())) {
            throw new RuntimeException("La factura ya se encuentra anulada");
        }

        invoice.setAnulada(true);

        InvoiceModel savedInvoice = invoiceRepository.save(invoice);

        return mapToResponseDto(savedInvoice);
    }

    private Long getNextFolio() {
        return invoiceRepository
                .findTopByOrderByFolioDesc()
                .map(invoice -> invoice.getFolio() + 1)
                .orElse(1L);
    }

    private InvoiceResponseDto mapToResponseDto(InvoiceModel invoice) {
        InvoiceResponseDto response = new InvoiceResponseDto();

        response.setId(invoice.getId());
        response.setSaleId(invoice.getSaleId());
        response.setFolio(invoice.getFolio());
        response.setFecha(invoice.getFecha());

        response.setRazonSocialReceptor(invoice.getRazonSocialReceptor());
        response.setGiroReceptor(invoice.getGiroReceptor());
        response.setDireccionReceptor(invoice.getDireccionReceptor());
        response.setRutReceptor(invoice.getRutReceptor());

        response.setRazonSocialEmisor(invoice.getRazonSocialEmisor());
        response.setGiroEmisor(invoice.getGiroEmisor());
        response.setDireccionEmisor(invoice.getDireccionEmisor());
        response.setRutEmisor(invoice.getRutEmisor());

        response.setMontoNeto(invoice.getMontoNeto());
        response.setIva(invoice.getIva());
        response.setMontoTotal(invoice.getMontoTotal());
        response.setAnulada(invoice.getAnulada());

        return response;
    }
}

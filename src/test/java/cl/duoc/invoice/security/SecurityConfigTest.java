/*
 * Copyright © 2026 DuocUC FullStack 1
 * Eduardo Bray
 * Rodrigo Callealta
 * Fernando Villalobos
 */
package cl.duoc.invoice.security;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import cl.duoc.invoice.dto.response.InvoiceResponseDto;
import cl.duoc.invoice.service.InvoiceService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.time.Instant;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@TestPropertySource(properties = "jwt.public=/actuator/**")
class SecurityConfigTest {

    private static final String VALID_INVOICE = """
            {"saleId":1,"fecha":"2026-11-25",
             "razonSocialReceptor":"Cliente SPA","giroReceptor":"Retail",
             "direccionReceptor":"Av. Siempre Viva 123","rutReceptor":"11.111.111-1",
             "razonSocialEmisor":"jKiltro SPA","giroEmisor":"Mascotas",
             "direccionEmisor":"Av. Principal 456","rutEmisor":"76.543.210-K",
             "montoNeto":10000}
            """;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private InvoiceService invoiceService;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @BeforeEach
    void stubService() {
        InvoiceResponseDto invoice = new InvoiceResponseDto();
        when(invoiceService.getAllInvoices()).thenReturn(List.of(invoice));
        when(invoiceService.getInvoiceByFolio(anyLong())).thenReturn(invoice);
        when(invoiceService.getInvoicesBySaleId(anyLong())).thenReturn(List.of(invoice));
        when(invoiceService.createInvoice(any())).thenReturn(invoice);
        when(invoiceService.anularInvoice(anyLong())).thenReturn(invoice);
    }

    private String tokenWithRole(String role) {
        return "Bearer "
                + JWT.create()
                        .withIssuer(issuer)
                        .withSubject("tester@duocuc.cl")
                        .withClaim("roles", List.of(role))
                        .withExpiresAt(Instant.now().plusSeconds(300))
                        .sign(Algorithm.HMAC256(secret));
    }

    @Test
    void listInvoices_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/invoices")).andExpect(status().isUnauthorized());
    }

    @Test
    void listInvoices_withInvalidToken_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/invoices").header(HttpHeaders.AUTHORIZATION, "Bearer token-invalido"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void listInvoices_asCustomer_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/invoices").header(HttpHeaders.AUTHORIZATION, tokenWithRole("user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void listInvoices_asAdmin_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/invoices").header(HttpHeaders.AUTHORIZATION, tokenWithRole("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void listInvoices_asSeller_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/invoices").header(HttpHeaders.AUTHORIZATION, tokenWithRole("vendedor")))
                .andExpect(status().isOk());
    }

    @Test
    void getInvoiceByFolio_asCustomer_returns403() throws Exception {
        mockMvc.perform(get("/api/v1/invoices/1").header(HttpHeaders.AUTHORIZATION, tokenWithRole("user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void getInvoiceByFolio_asAdmin_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/invoices/1").header(HttpHeaders.AUTHORIZATION, tokenWithRole("admin")))
                .andExpect(status().isOk());
    }

    @Test
    void getInvoicesBySale_asCustomer_returns200() throws Exception {
        mockMvc.perform(get("/api/v1/invoices/sale/1").header(HttpHeaders.AUTHORIZATION, tokenWithRole("user")))
                .andExpect(status().isOk());
    }

    @Test
    void getInvoicesBySale_withoutToken_returns401() throws Exception {
        mockMvc.perform(get("/api/v1/invoices/sale/1")).andExpect(status().isUnauthorized());
    }

    @Test
    void createInvoice_withoutToken_returns401() throws Exception {
        mockMvc.perform(post("/api/v1/invoices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_INVOICE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void createInvoice_asCustomer_returns201() throws Exception {
        mockMvc.perform(post("/api/v1/invoices")
                        .header(HttpHeaders.AUTHORIZATION, tokenWithRole("user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_INVOICE))
                .andExpect(status().isCreated());
    }

    @Test
    void anularInvoice_asCustomer_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/invoices/1/anular").header(HttpHeaders.AUTHORIZATION, tokenWithRole("user")))
                .andExpect(status().isForbidden());
    }

    @Test
    void anularInvoice_asSeller_returns403() throws Exception {
        mockMvc.perform(put("/api/v1/invoices/1/anular").header(HttpHeaders.AUTHORIZATION, tokenWithRole("vendedor")))
                .andExpect(status().isForbidden());
    }

    @Test
    void anularInvoice_asAdmin_returns200() throws Exception {
        mockMvc.perform(put("/api/v1/invoices/1/anular").header(HttpHeaders.AUTHORIZATION, tokenWithRole("admin")))
                .andExpect(status().isOk());
    }
}

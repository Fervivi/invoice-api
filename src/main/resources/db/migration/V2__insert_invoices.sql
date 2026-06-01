INSERT INTO invoices (
    sale_id,
    fecha,
    folio,
    razon_social_receptor,
    giro_receptor,
    direccion_receptor,
    rut_receptor,
    razon_social_emisor,
    giro_emisor,
    direccion_emisor,
    rut_emisor,
    monto_neto,
    iva,
    monto_total,
    anulada
) VALUES
(1, '2026-05-01', 1, 'Cliente Uno SPA', 'Retail', 'Av. Providencia 100', '11.111.111-1',
 'Proveedor Tech SPA', 'Tecnología', 'Av. Apoquindo 500', '76.123.456-7',
 100000, 19000, 119000, FALSE),

(2, '2026-05-02', 2, 'Cliente Dos Ltda', 'Servicios', 'Gran Avenida 200', '22.222.222-2',
 'Proveedor Tech SPA', 'Tecnología', 'Av. Apoquindo 500', '76.123.456-7',
 150000, 28500, 178500, FALSE),

(3, '2026-05-03', 3, 'Cliente Tres SPA', 'Comercial', 'Av. Las Condes 300', '33.333.333-3',
 'Proveedor Industrial Ltda', 'Industrial', 'Camino Melipilla 1000', '77.987.654-3',
 200000, 38000, 238000, FALSE),

(4, '2026-05-04', 4, 'Cliente Cuatro EIRL', 'Logística', 'Av. Matta 400', '44.444.444-4',
 'Proveedor Industrial Ltda', 'Industrial', 'Camino Melipilla 1000', '77.987.654-3',
 250000, 47500, 297500, FALSE),

(5, '2026-05-05', 5, 'Cliente Cinco SPA', 'Marketing', 'Av. Vicuña Mackenna 500', '55.555.555-5',
 'Proveedor Servicios SPA', 'Consultoría', 'Av. Kennedy 1200', '78.654.321-0',
 300000, 57000, 357000, FALSE),

(6, '2026-05-06', 6, 'Cliente Seis Ltda', 'Educación', 'Av. Grecia 600', '66.666.666-6',
 'Proveedor Servicios SPA', 'Consultoría', 'Av. Kennedy 1200', '78.654.321-0',
 180000, 34200, 214200, FALSE),

(7, '2026-05-07', 7, 'Cliente Siete SPA', 'Salud', 'Av. Independencia 700', '77.777.777-7',
 'Proveedor Médica Ltda', 'Salud', 'Av. Recoleta 900', '79.111.222-3',
 500000, 95000, 595000, FALSE),

(8, '2026-05-08', 8, 'Cliente Ocho Ltda', 'Construcción', 'Av. Departamental 800', '88.888.888-8',
 'Proveedor Construcción SPA', 'Construcción', 'Av. La Florida 1400', '80.222.333-4',
 420000, 79800, 499800, FALSE),

(9, '2026-05-09', 9, 'Cliente Nueve SPA', 'Minería', 'Av. Alameda 900', '99.999.999-9',
 'Proveedor Minero Ltda', 'Minería', 'Av. Vitacura 1800', '81.333.444-5',
 650000, 123500, 773500, FALSE),

(10, '2026-05-10', 10, 'Cliente Diez EIRL', 'Transporte', 'Av. Santa Rosa 1000', '10.101.010-1',
 'Proveedor Transporte SPA', 'Logística', 'Av. Américo Vespucio 2000', '82.444.555-6',
 275000, 52250, 327250, FALSE);
-- SubastaWeb v4 - T1-17 Semillas de categorías (Sprint 1)
-- Basado en Docs/07_Base_de_Datos/V2__categorias.sql (modelo oficial)
-- Requiere V1 (categories). Sin cuentas ni contraseñas (RN-01).
INSERT INTO categories (name, slug) VALUES
 ('Electrónica', 'electronica'),
 ('Libros', 'libros'),
 ('Hogar', 'hogar'),
 ('Deportes', 'deportes'),
 ('Otros', 'otros')
ON CONFLICT (slug) DO NOTHING;

-- SubastaWeb v4. V2__categorias.sql. No incluir cuentas ni password hashes de ejemplo.
INSERT INTO categories(name,slug) VALUES
 ('Electrónica','electronica'),
 ('Libros','libros'),
 ('Hogar','hogar'),
 ('Deportes','deportes'),
 ('Otros','otros')
ON CONFLICT (slug) DO NOTHING;

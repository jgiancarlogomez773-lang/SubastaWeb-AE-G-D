-- CONSULTAS DE CONTROL (lectura) - ejecutar sobre datos de prueba ya cargados.
-- 1. Catalogo no filtra privados: este SELECT debe usar solo estados publicos.
SELECT id,title,status,starts_at,ends_at FROM auctions
 WHERE status IN ('ACTIVA','FINALIZADA') ORDER BY created_at DESC LIMIT 50;

-- 2. Inconsistencia de resultado: debe retornar cero filas.
SELECT a.id,a.winning_bid_id,b.auction_id
 FROM auctions a JOIN bids b ON b.id=a.winning_bid_id
 WHERE b.auction_id<>a.id;

-- 3. Pujas de vendedor (logica backend): debe retornar cero filas.
SELECT b.id,b.auction_id,b.bidder_id FROM bids b
 JOIN auctions a ON a.id=b.auction_id WHERE a.seller_id=b.bidder_id;

-- 4. Pujas posteriores a vencimiento (logica backend): cero filas.
SELECT b.id,b.auction_id,b.placed_at,a.ends_at
 FROM bids b JOIN auctions a ON a.id=b.auction_id
 WHERE a.ends_at IS NOT NULL AND b.placed_at >= a.ends_at;

-- 5. Ganador que no es maxima oferta: cero filas.
SELECT a.id,a.winning_bid_id,b.amount,MAX(x.amount) AS max_amount
 FROM auctions a JOIN bids b ON b.id=a.winning_bid_id
 JOIN bids x ON x.auction_id=a.id
 WHERE a.status='FINALIZADA'
 GROUP BY a.id,a.winning_bid_id,b.amount
 HAVING b.amount<>MAX(x.amount);

-- 6. Estado cerrado y fecha faltante: cero filas por CHECK.
SELECT id FROM auctions WHERE status='FINALIZADA' AND closed_at IS NULL;

-- 7. Resumen de rondas de aprobacion y rechazo.
SELECT auction_id,review_round,decision,reviewed_at FROM moderation_reviews
 ORDER BY auction_id,review_round;

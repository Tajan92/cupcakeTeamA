BEGIN;

-- 1. CREATE TABLES
CREATE TABLE IF NOT EXISTS public.users (
                                            user_id bigserial NOT NULL,
                                            password character varying NOT NULL,
                                            email character varying NOT NULL,
                                            role character varying NOT NULL,
                                            balance double precision NOT NULL DEFAULT 0,
                                            CONSTRAINT users_pkey PRIMARY KEY (user_id)
);

CREATE TABLE IF NOT EXISTS public.cupcake (
                                              cupcake_id bigserial NOT NULL,
                                              cupcake_name character varying NOT NULL,
                                              price double precision NOT NULL,
                                              top character varying NOT NULL,
                                              bottom character varying NOT NULL,
                                              CONSTRAINT cupcake_pkey PRIMARY KEY (cupcake_id)
);

CREATE TABLE IF NOT EXISTS public.basket (
                                             basket_id bigserial NOT NULL,
                                             user_id bigint NOT NULL,
                                             CONSTRAINT basket_pkey PRIMARY KEY (basket_id),
                                             CONSTRAINT basket_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (user_id)
);

CREATE TABLE IF NOT EXISTS public.basket_cupcake (
                                                     line_id bigserial NOT NULL,
                                                     basket_id bigint NOT NULL,
                                                     cupcake_id bigint NOT NULL,
                                                     quantity integer NOT NULL,
                                                     CONSTRAINT basket_cupcake_pk PRIMARY KEY (line_id),
                                                     CONSTRAINT unique_basket_cupcake UNIQUE (basket_id, cupcake_id),
                                                     CONSTRAINT basket_cupcake_basket_id_fkey FOREIGN KEY (basket_id) REFERENCES public.basket (basket_id),
                                                     CONSTRAINT basket_cupcake_cupcake_id_fkey FOREIGN KEY (cupcake_id) REFERENCES public.cupcake (cupcake_id)
);

CREATE TABLE IF NOT EXISTS public.orders (
                                             order_id bigserial NOT NULL,
                                             user_id bigint NOT NULL,
                                             CONSTRAINT order_pkey PRIMARY KEY (order_id),
                                             CONSTRAINT order_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users (user_id)
);

CREATE TABLE IF NOT EXISTS public.order_items (
                                                  order_item_id bigserial NOT NULL,
                                                  order_id bigint NOT NULL,
                                                  cupcake_id bigint NOT NULL,
                                                  quantity integer NOT NULL,
                                                  CONSTRAINT order_items_pkey PRIMARY KEY (order_item_id),
                                                  CONSTRAINT fk_cupcake FOREIGN KEY (cupcake_id) REFERENCES public.cupcake (cupcake_id),
                                                  CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES public.orders (order_id)
);

-- 2. INSERT DATA (Ordered by dependencies)

-- USERS
INSERT INTO public.users (user_id, email, password, role, balance) VALUES
                                                                       (1, 'alex.smith@example.com', '12345hej-', 'user', 500),
                                                                       (2, 'jordan.lee@example.com', '12345hej-', 'user', 500),
                                                                       (3, 'sam.taylor@example.com', '12345hej-', 'user', 500),
                                                                       (4, 'casey.morgan@example.com', '12345hej-', 'user', 500),
                                                                       (5, 'riley.quinn@example.com', '12345hej-', 'user', 500),
                                                                       (6, 'morgan.bailey@example.com', '12345hej-', 'user', 500),
                                                                       (7, 'charlie.vance@example.com', '12345hej-', 'user', 500),
                                                                       (8, 'skyler.gray@example.com', '12345hej-', 'user', 500),
                                                                       (9, 'parker.reed@example.com', '12345hej-', 'user', 500),
                                                                       (10, 'u', 'u', 'user', 500),
                                                                       (11, 'a', 'a', 'admin', 0);

-- CUPCAKES
INSERT INTO public.cupcake (cupcake_id, cupcake_name, price, top, bottom) VALUES
                                                                              (1,'Double Trouble',10,'Chocolate (5)','Chocolate (5)'),(2,'Midnight Garden',10,'Blueberry (5)','Chocolate (5)'),(3,'Black Forest Kiss',10,'Raspberry (5)','Chocolate (5)'),(4,'Cocoa Crunch',11,'Crispy (6)','Chocolate (5)'),(5,'Dipped Delight',11,'Strawberry (6)','Chocolate (5)'),(6,'The Rum Runner',12,'Rum/Raisin (7)','Chocolate (5)'),(7,'Jaffa Dreamer',13,'Orange (8)','Chocolate (5)'),(8,'Dark & Sunny',13,'Lemon (8)','Chocolate (5)'),(9,'The Eccentric',14,'Blue cheese (9)','Chocolate (5)'),(10,'Black & White',10,'Chocolate (5)','Vanilla (5)'),(11,'Morning Cloud',10,'Blueberry (5)','Vanilla (5)'),(12,'Pink Lemonade',10,'Raspberry (5)','Vanilla (5)'),(13,'Snow Crackle',11,'Crispy (6)','Vanilla (5)'),(14,'Meadow & Cream',11,'Strawberry (6)','Vanilla (5)'),(15,'Rum Blanc',12,'Rum/Raisin (7)','Vanilla (5)'),(16,'Creamsicle Dream',13,'Orange (8)','Vanilla (5)'),(17,'Côte d''Azur',13,'Lemon (8)','Vanilla (5)'),(18,'The Contrarian',14,'Blue cheese (9)','Vanilla (5)'),(19,'Spice & Dark',10,'Chocolate (5)','Nutmeg (5)'),(20,'Nordic Fog',10,'Blueberry (5)','Nutmeg (5)'),(21,'The Mulled One',10,'Raspberry (5)','Nutmeg (5)'),(22,'Harvest Crunch',11,'Crispy (6)','Nutmeg (5)'),(23,'Spiced Valentine',11,'Strawberry (6)','Nutmeg (5)'),(24,'The Old Spice',12,'Rum/Raisin (7)','Nutmeg (5)'),(25,'Winter Solstice',13,'Orange (8)','Nutmeg (5)'),(26,'Golden Hour',13,'Lemon (8)','Nutmeg (5)'),(27,'The Cheeseboarder',14,'Blue cheese (9)','Nutmeg (5)'),(28,'Green Velvet',11,'Chocolate (5)','Pistachio (6)'),(29,'Persian Garden',11,'Blueberry (5)','Pistachio (6)'),(30,'Sicilian Blush',11,'Raspberry (5)','Pistachio (6)'),(31,'The Nutcracker',12,'Crispy (6)','Pistachio (6)'),(32,'Baklava Bliss',12,'Strawberry (6)','Pistachio (6)'),(33,'Merchant''s Secret',13,'Rum/Raisin (7)','Pistachio (6)'),(34,'Green & Gold',14,'Orange (8)','Pistachio (6)'),(35,'Amalfi Moment',14,'Lemon (8)','Pistachio (6)'),(36,'The Sommelier',15,'Blue cheese (9)','Pistachio (6)'),(37,'Toasted Glory',12,'Chocolate (5)','Almond (7)'),(38,'Marzipan Meadow',12,'Blueberry (5)','Almond (7)'),(39,'Linzer Tart',12,'Raspberry (5)','Almond (7)'),(40,'Praline Snap',13,'Crispy (6)','Almond (7)'),(41,'Cherry Blossom',13,'Strawberry (6)','Almond (7)'),(42,'The Amaretto',14,'Rum/Raisin (7)','Almond (7)'),(43,'Valencia',15,'Orange (8)','Almond (7)'),(44,'Frangipane Fizz',15,'Lemon (8)','Almond (7)'),(45,'The Pâtissier',16,'Blue cheese (9)','Almond (7)');

-- BASKETS
INSERT INTO public.basket (basket_id, user_id) VALUES
                                                   (1,1),(2,2),(3,3),(4,4),(5,5),(6,6),(7,7),(8,8),(9,9),(10,10);

-- BASKET ITEMS
INSERT INTO public.basket_cupcake (basket_id, cupcake_id, quantity, line_id) VALUES
                                                                                 (1,14,2,1),(1,39,1,2),(2,2,6,3),(2,45,3,4),(2,18,1,5),(3,7,4,6),(4,22,12,7),(4,5,2,8),(5,31,5,9),(5,11,3,10),(5,27,1,11),(6,40,6,12),(7,8,2,13),(7,33,4,14),(8,16,1,15),(8,42,2,16),(9,1,10,17),(9,29,3,18),(10,13,5,19),(10,36,2,20),(10,4,1,21);

-- ORDERS
INSERT INTO public.orders (order_id, user_id) VALUES
                                                  (1,1),(2,1),(3,1),(4,1),(5,1),(6,2),(7,2),(8,2),(9,2),(10,2),(11,3),(12,3),(13,3),(14,3),(15,3),(16,4),(17,4),(18,4),(19,4),(20,4),(21,5),(22,5),(23,5),(24,5),(25,5),(26,6),(27,6),(28,6),(29,6),(30,6),(31,7),(32,7),(33,7),(34,7),(35,7),(36,8),(37,8),(38,8),(39,8),(40,8),(41,9),(42,9),(43,9),(44,9),(45,9),(46,10),(47,10),(48,10),(49,10),(50,10);

-- ORDER ITEMS
INSERT INTO public.order_items (order_item_id, order_id, cupcake_id, quantity) VALUES
                                                                                   (1,1,12,2),(2,1,45,4),(3,2,7,6),(4,2,22,1),(5,3,33,12),(6,4,5,3),(7,4,18,2),(8,5,40,6),(9,6,1,1),(10,6,29,4),(11,7,15,2),(12,8,38,5),(13,9,44,1),(14,9,3,8),(15,10,21,2),(16,11,10,4),(17,11,36,1),(18,12,25,3),(19,13,9,6),(20,14,42,2),(21,14,14,2),(22,15,31,12),(23,16,2,4),(24,16,43,1),(25,17,11,5),(26,18,20,2),(27,18,37,3),(28,19,6,1),(29,20,28,6),(30,21,4,2),(31,21,19,4),(32,22,41,1),(33,23,13,5),(34,23,32,2),(35,24,8,3),(36,25,26,12),(37,26,45,1),(38,26,17,4),(39,27,23,2),(40,28,35,6),(41,28,30,1),(42,29,16,5),(43,30,39,2),(44,31,24,3),(45,31,1,8),(46,32,34,1),(47,33,12,4),(48,33,44,2),(49,34,7,6),(50,35,21,2),(51,36,3,5),(52,36,18,1),(53,37,40,3),(54,38,10,12),(55,38,29,2),(56,39,15,4),(57,40,36,1),(58,41,22,6),(59,41,5,2),(60,42,33,5),(61,43,9,3),(62,43,42,1),(63,44,25,4),(64,45,14,8),(65,45,38,2),(66,46,31,1),(67,46,2,6),(68,47,43,2),(69,48,11,4),(70,48,37,3),(71,49,20,5),(72,50,6,12),(73,50,28,1),(74,1,4,2),(75,5,19,4),(76,10,41,1),(77,15,13,3),(78,20,32,6),(79,25,8,2),(80,30,26,5),(81,35,17,1),(82,40,23,4),(83,45,35,2),(84,49,30,3),(85,50,16,6);

-- 3. RESET SEQUENCES
-- This ensures that your Java code won't crash when it tries to insert ID 11 or 51.
SELECT setval(pg_get_serial_sequence('public.users', 'user_id'), (SELECT MAX(user_id) FROM public.users));
SELECT setval(pg_get_serial_sequence('public.cupcake', 'cupcake_id'), (SELECT MAX(cupcake_id) FROM public.cupcake));
SELECT setval(pg_get_serial_sequence('public.basket', 'basket_id'), (SELECT MAX(basket_id) FROM public.basket));
SELECT setval(pg_get_serial_sequence('public.basket_cupcake', 'line_id'), (SELECT MAX(line_id) FROM public.basket_cupcake));
SELECT setval(pg_get_serial_sequence('public.orders', 'order_id'), (SELECT MAX(order_id) FROM public.orders));
SELECT setval(pg_get_serial_sequence('public.order_items', 'order_item_id'), (SELECT MAX(order_item_id) FROM public.order_items));

COMMIT;
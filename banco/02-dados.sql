INSERT INTO categoria (nome, descricao) VALUES ('ELETRONICOS', 'Produtos relacionados a tecnologia e dispositivos eletrônicos.');
INSERT INTO categoria (nome, descricao) VALUES ('ROUPAS', 'Vestuário e acessórios de moda.');
INSERT INTO categoria (nome, descricao) VALUES ('ALIMENTOS', 'Produtos alimentícios e bebidas.');
INSERT INTO categoria (nome, descricao) VALUES ('LIVROS', 'Livros de diversos gêneros e temas.');
INSERT INTO categoria (nome, descricao) VALUES ('MOVEIS', 'Móveis para casa e escritório.');
INSERT INTO categoria (nome, descricao) VALUES ('BRINQUEDOS', 'Brinquedos e jogos para crianças.');

INSERT INTO usuario (username, password, role) VALUES
                                                   ('joao_silva', '$2a$10$Q745xaC7FCMv3qAwrErBKerosbOVr5hCGHI20Qy2DpJ6Runs91XgC', 'USER'),
                                                   ('maria_oliveira', '$2a$10$s2h12Kva.xG2uvwzo.c98.3Jc9uYA52KQSCJiMuByrKCTjeshFpfa', 'USER'),
                                                   ('pedro_santos', '$2a$10$WZjLyd4XcLI4iSd3QnzDfeXjsEDqrLsTcCvNwk2FR6KRD8nVXJfay', 'USER'),
                                                   ('ana_costa', '$2a$10$e3e.1I.1Pdtomqj5kCQoy.Um7aYluctqEEeZx.AyrClOkDlDJ.qyK', 'USER'),
                                                   ('lucas_martins', '$2a$10$QaiefPxcW0tX5AK1WMsxROCIzZM19wC8wrR6AMNQNIA4jlWgMKcsG', 'USER'),
                                                   ('juliana_lima', '$2a$10$noHfnM6RYCKyIqofsv0LNOZKhsNW.pMWRvQnktEiBEhc16zGiBY.m', 'USER'),
                                                   ('rafael_souza', '$2a$10$o9/CgSZxheezY5w2redMq.T/JcZLwXim4JcgIBMrRYlfda.KpHpfO', 'USER'),
                                                   ('carla_rocha', '$2a$10$VF2.o8PB7Tf2uMPsgqaSVuYCNSQ2bcDlxG79ZPNR9UJ3W5m2goMWS', 'USER'),
                                                   ('fernando_gomes', '$2a$10$KIzVTzO5Sy.PjcC306mhHOsjKw2YmenAog8Jti9wXlYPHilnUMI6q', 'USER'),
                                                   ('admin_master', '$2a$10$qSJKImk1kJte2c5Ms1cI4uIfPywOLiGg8oCpS8hFxCA6g08ycXtWC', 'ADMIN');

INSERT INTO produto (nome, descricao, preco, categoria, quantidade_estoque, id_categoria) VALUES
                                                                                              ('Smartphone X200', 'Celular com 128GB e câmera dupla', 1999.90, 'ELETRONICOS', 25, 1),
                                                                                              ('Notebook Pro 15', 'Notebook com processador i7 e SSD 512GB', 5299.00, 'ELETRONICOS', 10, 1),
                                                                                              ('Fone Bluetooth A5', 'Fone sem fio com cancelamento de ruído', 299.90, 'ELETRONICOS', 40, 1),
                                                                                              ('Camiseta Básica', 'Camiseta 100% algodão', 39.90, 'ROUPAS', 100, 2),
                                                                                              ('Calça Jeans Slim', 'Calça jeans azul masculina', 129.90, 'ROUPAS', 60, 2),
                                                                                              ('Vestido Floral', 'Vestido leve com estampa floral', 149.90, 'ROUPAS', 30, 2),
                                                                                              ('Tênis Runner', 'Tênis esportivo confortável', 249.90, 'ROUPAS', 50, 2),
                                                                                              ('Chocolate Amargo 70%', 'Barra de chocolate premium 100g', 14.90, 'ALIMENTOS', 200, 3),
                                                                                              ('Café Gourmet 500g', 'Café moído 100% arábica', 29.90, 'ALIMENTOS', 80, 3),
                                                                                              ('Biscoito Integral', 'Biscoito saudável com fibras', 9.90, 'ALIMENTOS', 150, 3),
                                                                                              ('Livro "Java Essencial"', 'Guia completo de programação Java', 89.90, 'LIVROS', 40, 4),
                                                                                              ('Livro "Clean Code"', 'Boas práticas de programação', 119.90, 'LIVROS', 30, 4),
                                                                                              ('Livro "Design Patterns"', 'Padrões de projeto em Java', 99.90, 'LIVROS', 25, 4),
                                                                                              ('Cadeira Gamer Xtreme', 'Cadeira ergonômica com apoio lombar', 999.90, 'MOVEIS', 15, 5),
                                                                                              ('Mesa de Escritório', 'Mesa compacta em MDF', 399.90, 'MOVEIS', 20, 5),
                                                                                              ('Estante Modular', 'Estante com 4 nichos', 249.90, 'MOVEIS', 30, 5),
                                                                                              ('Carrinho de Controle Remoto', 'Carrinho elétrico infantil', 189.90, 'BRINQUEDOS', 40, 6),
                                                                                              ('Boneca Fashion', 'Boneca articulada com acessórios', 79.90, 'BRINQUEDOS', 50, 6),
                                                                                              ('Jogo de Tabuleiro', 'Jogo de estratégia para família', 99.90, 'BRINQUEDOS', 35, 6),
                                                                                              ('Drone MiniCam', 'Drone com câmera HD', 599.90, 'ELETRONICOS', 15, 1),
                                                                                              ('Headset Gamer', 'Headset com microfone e LED', 249.90, 'ELETRONICOS', 25, 1),
                                                                                              ('Smartwatch Fit', 'Relógio inteligente com monitor cardíaco', 499.90, 'ELETRONICOS', 18, 1),
                                                                                              ('Jaqueta Jeans', 'Jaqueta unissex azul clara', 189.90, 'ROUPAS', 40, 2),
                                                                                              ('Camisa Social', 'Camisa slim masculina branca', 129.90, 'ROUPAS', 35, 2),
                                                                                              ('Tênis Casual', 'Tênis leve para o dia a dia', 199.90, 'ROUPAS', 40, 2),
                                                                                              ('Arroz Integral 1kg', 'Pacote de arroz integral premium', 8.90, 'ALIMENTOS', 100, 3),
                                                                                              ('Feijão Preto 1kg', 'Feijão selecionado tipo 1', 7.90, 'ALIMENTOS', 100, 3),
                                                                                              ('Suco Natural 1L', 'Suco de frutas sem conservantes', 12.90, 'ALIMENTOS', 60, 3),
                                                                                              ('Livro "Spring Boot Avançado"', 'Desenvolvimento web moderno em Java', 129.90, 'LIVROS', 20, 4),
                                                                                              ('Livro "Microservices"', 'Arquitetura de microsserviços', 139.90, 'LIVROS', 25, 4),
                                                                                              ('Poltrona Confort', 'Poltrona reclinável de tecido', 899.90, 'MOVEIS', 10, 5),
                                                                                              ('Sofá Retrátil 3 Lugares', 'Sofá com design moderno', 1799.90, 'MOVEIS', 5, 5),
                                                                                              ('Mesa de Cabeceira', 'Mesa lateral com gaveta', 199.90, 'MOVEIS', 15, 5),
                                                                                              ('Lego Criativo', 'Blocos de montar 500 peças', 149.90, 'BRINQUEDOS', 25, 6),
                                                                                              ('Puzzle 1000 Peças', 'Quebra-cabeça paisagem', 79.90, 'BRINQUEDOS', 50, 6),
                                                                                              ('Jogo Educativo', 'Aprendizado divertido para crianças', 59.90, 'BRINQUEDOS', 45, 6),
                                                                                              ('Tablet Kids', 'Tablet infantil com controle parental', 699.90, 'ELETRONICOS', 20, 1),
                                                                                              ('Câmera Digital Compacta', 'Câmera com 20MP', 899.90, 'ELETRONICOS', 10, 1),
                                                                                              ('Teclado Mecânico RGB', 'Teclado gamer com iluminação', 349.90, 'ELETRONICOS', 30, 1),
                                                                                              ('Camisa Polo', 'Camisa polo clássica', 99.90, 'ROUPAS', 50, 2),
                                                                                              ('Moletom Unissex', 'Moletom confortável com capuz', 159.90, 'ROUPAS', 35, 2),
                                                                                              ('Bolacha Recheada', 'Pacote com 10 unidades', 5.90, 'ALIMENTOS', 200, 3),
                                                                                              ('Cereal Matinal', 'Cereal com grãos integrais', 12.90, 'ALIMENTOS', 90, 3),
                                                                                              ('Livro "Kotlin para Java Devs"', 'Aprenda Kotlin vindo do Java', 99.90, 'LIVROS', 15, 4),
                                                                                              ('Livro "Arquitetura Limpa"', 'Organização de código eficiente', 119.90, 'LIVROS', 20, 4),
                                                                                              ('Cadeira de Escritório', 'Cadeira ergonômica ajustável', 599.90, 'MOVEIS', 12, 5),
                                                                                              ('Guarda-Roupa 3 Portas', 'Móvel em MDF com espelho', 1299.90, 'MOVEIS', 5, 5),
                                                                                              ('Boneco de Ação', 'Boneco colecionável articulado', 89.90, 'BRINQUEDOS', 40, 6),
                                                                                              ('Patinete Infantil', 'Patinete dobrável de alumínio', 159.90, 'BRINQUEDOS', 20, 6);

INSERT INTO pedido (usuario, status, valor_total) VALUES
                                                                   ('joao_silva', 'CONCLUIDO', 389.70),
                                                                   ('maria_oliveira', 'PENDENTE', 2599.70),
                                                                   ('pedro_santos', 'EM_PROCESSAMENTO', 199.80),
                                                                   ('ana_costa', 'CONCLUIDO', 749.70),
                                                                   ('lucas_martins', 'CANCELADO', 99.90),
                                                                   ('juliana_lima', 'CONCLUIDO', 899.70),
                                                                   ('rafael_souza', 'PENDENTE', 329.80),
                                                                   ('carla_rocha', 'EM_PROCESSAMENTO', 1299.90),
                                                                   ('fernando_gomes', 'CONCLUIDO', 699.90),
                                                                   ('joao_silva', 'CONCLUIDO', 1299.80);

-- Itens do pedido 1
INSERT INTO pedido_itens (id_pedido, id_produto, quantidade, preco_unitario) VALUES
                                                                                 (1, 4, 2, 39.90),
                                                                                 (1, 8, 3, 14.90),
                                                                                 (1, 19, 1, 99.90);

-- Itens do pedido 2
INSERT INTO pedido_itens (id_pedido, id_produto, quantidade, preco_unitario) VALUES
                                                                                 (2, 1, 1, 1999.90),
                                                                                 (2, 20, 2, 299.90);

-- Itens do pedido 3
INSERT INTO pedido_itens (id_pedido, id_produto, quantidade, preco_unitario) VALUES
                                                                                 (3, 9, 4, 29.90),
                                                                                 (3, 10, 5, 9.90);

-- Itens do pedido 4
INSERT INTO pedido_itens (id_pedido, id_produto, quantidade, preco_unitario) VALUES
                                                                                 (4, 11, 2, 89.90),
                                                                                 (4, 13, 1, 99.90),
                                                                                 (4, 24, 3, 129.90);

-- Itens do pedido 10
INSERT INTO pedido_itens (id_pedido, id_produto, quantidade, preco_unitario) VALUES (10, 24, 10, 129.90);


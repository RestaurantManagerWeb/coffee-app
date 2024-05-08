-- тестовые данные для профиля prod (без проверки через сервисы)

-- unit_measure
INSERT INTO unit_measure(name, symbol) VALUES ('масса', 'г');
INSERT INTO unit_measure(name, symbol) VALUES ('объем', 'мл');
INSERT INTO unit_measure(name, symbol) VALUES ('количество', 'шт.');

-- stock_item
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Лимон', 943, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Чай черный листовой', 280, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Черника с/м', 300, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Черная смородина с/м', 420, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Сахар белый', 3400, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Вода газ. 0.5 л пластик', 3, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Хлеб тостовый', 24, 3); -- id 7
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Салатная смесь', 315, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Огурец свежий', 227, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Яйцо куриное', 8, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Тунец консервированный рубленый', 483, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Вода н/г 0.5 л пластик', 21, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Зерно Бразилия 250 г', 3, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Зерно Бразилия', 8700, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('Вода минеральная газ.', 2400, 2);

-- processing_method
INSERT INTO processing_method(name, description) VALUES ('без обработки', null);
INSERT INTO processing_method(name, description) VALUES ('механическая обработка', 'сортировка, мытье, очистка, промывание, нарезка');
INSERT INTO processing_method(name, description) VALUES ('тепловая (варка)', null);
INSERT INTO processing_method(name, description) VALUES ('измельчение', null);

-- ingredient
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Вода питьевая', 0, null, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Лед', 0, null, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Лимон', 12, 1, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Чай черный листовой', 0, 2, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Черника', 0, 3, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Черная смородина', 0, 4, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Сахар', 0, 5, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Хлеб тостовый', 0, 7, 1); -- id 8
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Салатная смесь', 10, 8, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Огурец свежий', 5, 9, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Яйцо куриное отварное', 0, 10, 3);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Тунец рубленый', 22, 11, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('Вода газ.', 0, 15, 1);

-- process_chart
INSERT INTO process_chart(yield, portion, description) VALUES (300, DEFAULT, 'Добавить в стакан гостя дольку лимона, заварку в одноразовом пакетике, ягодное пюре и кипяток. Хорошо перемешать и удалить заварку');
INSERT INTO process_chart(yield, portion, description) VALUES (500, DEFAULT, 'Замороженные ягоды, сахар и кипяток взбить до однородной массы в блендере, процедить через сито');
INSERT INTO process_chart(yield, portion, description) VALUES (200, DEFAULT, 'Добавить в стакан гостя заварку в одноразовом пакетике и кипяток до нужного объема');
INSERT INTO process_chart(yield, portion, description) VALUES (210, 2, 'Тостовый хлеб подсушить на гриле, на один кусок положить равномерно зелень, огурец (слайсы), тунец, снова зелень, вареное яйцо, нарезанное кольцами. Затем накрыть вторым куском хлеба и разрезать по диагонали');

-- semi_finished
INSERT INTO semi_finished(name, process_chart_id) VALUES ('Пюре ягодное', 2);

-- recipe_composition
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (1, 3, null, 15);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (1, 4, null, 5);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (1, null, 1, 20);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (1, 1, null, 260);

INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (2, 5, null, 110);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (2, 6, null, 110);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (2, 7, null, 220);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (2, 1, null, 80);

INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (3, 4, null, 4);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (3, 1, null, 200);

INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 8, null, 1);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 9, null, 10);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 10, null, 20);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 12, null, 50);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 9, null, 10);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 11, null, 1);
INSERT INTO recipe_composition(process_chart_id, ingredient_id, semi_finished_id, netto) VALUES (4, 8, null, 1);

-- menu_group
INSERT INTO menu_group(name) VALUES('Чай');
INSERT INTO menu_group(name) VALUES('Покупные товары');
INSERT INTO menu_group(name) VALUES('Сэндвичи');
INSERT INTO menu_group(name) VALUES('Лимонады');

-- menu_item
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('Чай ягодный 300 мл', 220, DEFAULT, DEFAULT, 1, null, 1);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('Чай черный 200 мл', 100, DEFAULT, DEFAULT, 1, null, 3);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('Вода газ. 0.5 л пластик', 100, DEFAULT, DEFAULT, 2, 6, null);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('Тост с тунцом', 130, DEFAULT, DEFAULT, 3,  null, 4);

-- ordering
INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES('2024-01-15 14:22:23', null, 1);
INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES('2024-01-15 20:22:23', null, 2);
INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES('2024-01-18 21:22:23', null, 3);

INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES(NOW(), null, 4);
INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES(NOW(), null, 5);
INSERT INTO ordering(created_at, cancelled_at, receipt_id) VALUES(NOW(), null, 6);

-- shopping_cart
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(1, 1, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(2, 1, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(3, 1, 1);

INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(4, 1, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(5, 2, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(5, 1, 2);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(6, 4, 1);




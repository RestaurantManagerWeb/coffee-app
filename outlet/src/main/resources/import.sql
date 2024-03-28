-- unit_measure
INSERT INTO unit_measure(name, symbol) VALUES ('масса', 'г');
INSERT INTO unit_measure(name, symbol) VALUES ('объем', 'мл');
INSERT INTO unit_measure(name, symbol) VALUES ('количество', 'шт.');

-- stock_item
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('лимон', 943, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('чай черный листовой', 280, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('черника с/м', 300, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('черная смородина с/м', 420, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('сахар белый', 3400, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('вода газ. 0.5 л пластик', 3, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('хлеб тостовый', 24, 3); -- id 7
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('салатная смесь', 315, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('огурец свежий', 227, 1);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('яйцо куриное', 8, 3);
INSERT INTO stock_item(name, quantity, unit_measure_id) VALUES ('тунец консервированный рубленый', 483, 1);

-- processing_method
INSERT INTO processing_method(name, description) VALUES ('без обработки', null);
INSERT INTO processing_method(name, description) VALUES ('механическая', 'сортировка, мытье, очистка, промывание, нарезка');
INSERT INTO processing_method(name, description) VALUES ('тепловая (варка)', null);

-- ingredient
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('вода питьевая', 0, null, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('лед', 0, null, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('лимон', 12, 1, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('чай черный листовой', 0, 2, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('черника', 0, 3, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('черная смородина', 0, 4, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('сахар', 0, 5, 1);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('хлеб тостовый', 0, 7, 1); -- id 8
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('салатная смесь', 10, 8, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('огурец свежий', 5, 9, 2);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('яйцо куриное отварное', 0, 10, 3);
INSERT INTO ingredient(name, weight_loss, stock_item_id, processing_method_id) VALUES ('тунец рубленый', 22, 11, 1);

-- process_chart
INSERT INTO process_chart(yield, portion, description) VALUES (300, DEFAULT, 'Добавить в стакан гостя дольку лимона, заварку в одноразовом пакетике, ягодное пюре и кипяток. Хорошо перемешать и удалить заварку');
INSERT INTO process_chart(yield, portion, description) VALUES (500, DEFAULT, 'Замороженные ягоды, сахар и кипяток взбить до однородной массы в блендере, процедить через сито');
INSERT INTO process_chart(yield, portion, description) VALUES (200, DEFAULT, 'Добавить в стакан гостя заварку в одноразовом пакетике и кипяток до нужного объема');
INSERT INTO process_chart(yield, portion, description) VALUES (210, 2, 'Тостовый хлеб подсушить на гриле, на один кусок положить равномерно зелень, огурец (слайсы), тунец, снова зелень, вареное яйцо, нарезанное кольцами. Затем накрыть вторым куском хлеба и разрезать по диагонали');

-- semi_finished
INSERT INTO semi_finished(name, process_chart_id) VALUES ('пюре ягодное', 2);

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
INSERT INTO menu_group(name) VALUES('чай');
INSERT INTO menu_group(name) VALUES('покупные товары');
INSERT INTO menu_group(name) VALUES('сэндвичи');

-- menu_item
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('чай ягодный 300 мл', 220, DEFAULT, DEFAULT, 1, null, 1);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('чай черный 200 мл', 100, DEFAULT, DEFAULT, 1, null, 3);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('вода газ. 0.5 л пластик', 100, DEFAULT, DEFAULT, 2, 6, null);
INSERT INTO menu_item(name, price, vat, in_stop_list, menu_group_id, stock_item_id, process_chart_id) VALUES('тост с тунцом', 130, DEFAULT, DEFAULT, 3,  null, 4);

-- ordering
INSERT INTO ordering(created_on, cancelled_on, receipt_id) VALUES(NOW(), null, 1);
INSERT INTO ordering(created_on, cancelled_on, receipt_id) VALUES(NOW(), null, 2);
INSERT INTO ordering(created_on, cancelled_on, receipt_id) VALUES(NOW(), null, 3);

-- shopping_cart
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(1, 1, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(2, 2, 1);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(2, 1, 2);
INSERT INTO shopping_cart(ordering_id, menu_item_id, quantity) VALUES(3, 1, 1);




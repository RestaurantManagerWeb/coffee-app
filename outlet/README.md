# Сервис для работы с заказами на предприятии

## Схема базы данных

<img src="src/main/resources/static/SQL_Schema.PNG" alt="Схема SQL" width="900"/>

## База данных H2 (profile dev)

[Пользовательский интерфейс](http://localhost:8081/h2-ui/)

## Создание Docker контейнера с postgresql (profile prod)

Создание контейнера на порту 5435

* docker run --name psql-cafe -e POSTGRES_DB=outlet_db -e POSTGRES_USER=sa -e POSTGRES_PASSWORD=p -p 5435:5432
  -d postgres:15.5-bullseye

Вход в контейнер и в базу данных outlet_db

* docker exec -it psql-cafe psql -U sa outlet_db

Вход в базу данных внутри контейнера

* psql -U sa -d outlet_db

## Доступные endpoints

[springdoc-openapi:swagger](http://localhost:8081/swagger-ui/index.html)

1. [/menu (GET)](http://localhost:8081/menu) - получение списка групп меню

2. [/menu/group?id={1} (GET)](http://localhost:8081/menu/group?id=1) - получение списка
   позиций меню в группе по ID группы

3. [/menu/order (POST)](http://localhost:8081/menu/order) - получение ID заказа.
   Записать информацию о заказе в базу и рассчитать расход сырья.
   Данные: ID чека, пары "menuItemId":"quantity"

```json
{
  "receiptId": 5,
  "shoppingCartItems": {
    "1": 2,
    "2": 1,
    "3": 1
  }
}
```

4. [/outlet/stock/shipment (PUT)](http://localhost:8081/outlet/stock/shipment) - принятие
   новой поставки продуктов. Данные: ID продукта на складе в outlet (StockItem, Long),
   количество в штуках (без дробной части), миллилитрах или граммах (Double).
   Возвращает список непринятых позиций (ID продукта).

```json
{
  "1": 49,
  "3": 215.5,
  "6": 12
}
```

5. [/cook (GET)](http://localhost:8081/cook) - получение списка позиций меню и полуфабрикатов,
   для которых есть техкарта. Список разбит по группам, для каждой позиции указан ID техкарты

6. [/cook/{4} (GET)](http://localhost:8081/cook/4) - получение информации о техкарте по ID ТК. Для
   отображения списка ингредиентов его нужно будет отсортировать по ключу (по возрастанию).


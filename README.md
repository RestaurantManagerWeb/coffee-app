# Дипломный проект

## Тема

**_Разработка веб-приложения для небольшой кофейни с реализацией ограниченного ассортимента
продукции общественного питания с возможностью подключения нескольких филиалов одной сети._**

## Настройки проекта (Settings)

Maven project<br>
JDK: Amazon Corretto 17 (corretto-17)<br>
Language level: 17 (SDK default)<br>
Настройки запуска: .runConfigurations/runAll.run.xml

Микросервисы (в порядке запуска):

1. config-server - сервер для хранения настроек всех микросервисов (Spring Cloud Config)
2. eureka-server - сервер Eureka для обнаружения микросервисов (Spring Cloud Netflix - Eureka Server)
3. outlet-service - сервис для работы с заказами на предприятии
4. api-gateway - сервис для переадресации запросов от клиентского приложения микросервисам

Обращение к сервисам через `http://localhost:8765/{service_name}/{path}`

## Документация Springdoc OpenAPI (Swagger)

[springdoc-openapi:swagger](http://localhost:8765/swagger-ui.html)

## Eureka server

[eureka-server](http://localhost:8761/)

## Профили

Задание профиля в файле
[api-configs/application.yaml](server/config-server/src/main/resources/api-configs/application.yaml)

`spring.cloud.config.profile=dev`<br>
`spring.cloud.config.profile=prod`

### Profile dev: База данных H2

[Пользовательский интерфейс для БД Outlet](http://localhost:8081/h2-ui/)

### Profile prod: Создание Docker контейнера с postgresql

Создание контейнера на порту 5435

```shell
docker run --name psql-cafe -e POSTGRES_DB=outlet_db -e POSTGRES_USER=sa -e POSTGRES_PASSWORD=p -p 5435:5432 -d postgres:15.5-bullseye
```

Вход в контейнер и в базу данных outlet_db

```shell
docker exec -it psql-cafe psql -U sa outlet_db
```

Вход в базу данных внутри контейнера

```shell
psql -U sa -d outlet_db
```

## Доступные endpoints

### outlet-service (тестовые данные)

1. [(POST) /order](http://localhost:8765/outlet/order) - получение ID заказа.
   Записать информацию о заказе в базу и рассчитать расход сырья.

```json
{
  "receiptId": 8,
  "shoppingCartItems": [
    {
      "menuItemId": 1,
      "quantity": 2
    },
    {
      "menuItemId": 2,
      "quantity": 1
    },
    {
      "menuItemId": 3,
      "quantity": 1
    }
  ]
}
```

2. [(POST) /menu/group](http://localhost:8765/outlet/menu/group) - создание новой группы меню

```json
{
  "name": "кофе"
}
```

3. [(POST) /menu/item/stock](http://localhost:8765/outlet/menu/item/stock) - создание позиции меню, связанной
   с позицией на складе

```json
{
  "name": "вода н/г 0.5 л, пластик",
  "price": 100,
  "vat": 10,
  "menuGroupId": 2,
  "stockItemId": 12
}
```

```json
{
  "name": "Зерно Бразилия, 250 г",
  "price": 1700,
  "vat": 20,
  "menuGroupId": 2,
  "stockItemId": 13
}
```

4. [(POST) /stock/shipment](http://localhost:8765/outlet/stock/shipment) - принятие
   новой поставки продуктов. Данные: ID позиции на складе в outlet (StockItem, Long),
   количество в штуках (без дробной части), миллилитрах или граммах (Double).
   Возвращает список непринятых позиций (ID позиции).

```json
[
  {
    "stockItemId": 1,
    "quantity": 49
  },
  {
    "stockItemId": 3,
    "quantity": 215.5
  },
  {
    "stockItemId": 6,
    "quantity": 12
  }
]
```

5. [(POST) /stock](http://localhost:8765/outlet/stock) - создание новой позиции на складе

```json
{
  "name": "сыр творожный сливочный",
  "unitMeasureId": 1
}
```
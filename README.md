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

## Документация Springdoc OpenAPI

[springdoc-openapi:swagger](http://localhost:8765/swagger-ui.html)

## Eureka server

[eureka-server](http://localhost:8761/)

## Профили

Задание профиля в файле [api-configs/application.yaml](config-server/src/main/resources/api-configs/application.yaml)

`spring.cloud.config.profile=dev`<br>
`spring.cloud.config.profile=prod`

### Profile dev: База данных H2

[Пользовательский интерфейс для БД Outlet](http://localhost:8081/h2-ui/)

### Profile prod: Создание Docker контейнера с postgresql

Создание контейнера на порту 5435

* docker run --name psql-cafe -e POSTGRES_DB=outlet_db -e POSTGRES_USER=sa -e POSTGRES_PASSWORD=p -p 5435:5432
  -d postgres:15.5-bullseye

Вход в контейнер и в базу данных outlet_db

* docker exec -it psql-cafe psql -U sa outlet_db

Вход в базу данных внутри контейнера

* psql -U sa -d outlet_db

## Доступные endpoints

### outlet-service

1. [(GET) /menu](http://localhost:8765/outlet/menu) - получение списка групп меню

2. [(GET) /menu/group?id={1}](http://localhost:8765/outlet/menu/group?id=1) - получение списка
   позиций меню в группе по ID группы

3. [(POST) /menu/order](http://localhost:8765/outlet/menu/order) - получение ID заказа.
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

4. [(PUT) /outlet/stock/shipment](http://localhost:8765/outlet/stock/shipment) - принятие
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

5. [(GET) /cook](http://localhost:8765/outlet/cook) - получение списка позиций меню и полуфабрикатов,
   для которых есть техкарта. Список разбит по группам, для каждой позиции указан ID техкарты

6. [(GET) /cook/{4}](http://localhost:8765/outlet/cook/4) - получение информации о техкарте по ID ТК. Для
   отображения списка ингредиентов его нужно будет отсортировать по ключу (по возрастанию).


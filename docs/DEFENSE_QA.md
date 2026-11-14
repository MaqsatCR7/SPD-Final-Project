# Подготовка к защите (OOP + patterns) - 40% оценки

## Как объяснять проект за 1 минуту
«Это Library Management System на Java. Мы использовали 9 паттернов: Singleton, Factory Method, Builder (creational);
Adapter, Decorator, Facade (structural); Observer, Strategy, Command (behavioral). UI работает только через Facade,
действия — это Command с undo, штрафы считает Strategy, уведомления отправляет Observer.»

## Вопросы по паттернам
**Singleton — как сделан и зачем?** Holder-идиома: private конструктор, вложенный класс Holder со static final INSTANCE.
JVM создаёт Holder лениво и потокобезопасно при первом `getInstance()`. Нужен один общий `LibraryDatabase`.

**Factory Method vs Simple Factory vs Abstract Factory?** У нас `ItemCreator` (интерфейс) + `BookCreator/...`
(каждый создаёт свой тип), `ItemFactory` выбирает creator из EnumMap. Новый тип = новый creator, старый код не меняем (OCP).
Abstract Factory создаёт семейства связанных объектов — здесь не нужно.

**Зачем Builder?** У Member много необязательных полей; без Builder был бы конструктор с кучей параметров.
Builder читаемый (`.phone(..).membershipType(..).build()`), валидирует данные в `build()`.

**Adapter: class vs object adapter?** Мы используем object adapter (композиция): `LegacyCatalogAdapter` хранит `LegacyCatalogSystem`
и реализует `CatalogProvider`. Старый код менять нельзя — адаптер переводит формат `B|ID|TITLE|AUTHOR|YEAR` в `LibraryItem`.

**Decorator vs наследование?** Комбинации (Extended + SMS + Delivery) через наследование дали бы 2^3 классов.
Decorator оборачивает `LoanPolicy` и добавляет дни/цену/описание в любом сочетании на лету.

**Facade — зачем?** Скрывает сложность (БД, фабрика, builder, команды, сервис). UI знает только `LibraryFacade`.
Это не новая логика, а простой вход в подсистему.

**Observer — push или pull?** Push: `ItemEvent` передаётся в `update()`. Subject — `LibraryItem`, observers — `Member` и `AuditLogObserver`.
Используем `CopyOnWriteArrayList`, чтобы Member мог отписаться во время уведомления.

**Strategy vs State?** Strategy — взаимозаменяемые алгоритмы (штраф), выбирает клиент/контекст. State — объект сам меняет поведение
при смене состояния. У нас штраф зависит от типа членства → Strategy; выбор в `FineStrategyProvider` через EnumMap.

**Command — зачем?** Каждое действие — объект с `execute()/undo()`. `CommandHistory` хранит стек → undo и лог операций.
Receiver — `LibraryService`, Invoker — `CommandHistory`.

**Какие паттерны взаимодействуют?** Facade → Factory/Builder/Decorator/Command; Command → Service → Singleton;
Service → Observer (setAvailable); Service → Strategy (штраф); Adapter → Factory.

## Вопросы по OOP
- **Инкапсуляция:** поля private, доступ через геттеры, Member создаётся только Builder'ом (private конструктор).
- **Наследование:** `Book/Magazine/Dvd extends LibraryItem`.
- **Полиморфизм:** `item.getBaseLoanDays()`, `strategy.calculate()`, `command.execute()` — вызывается нужная реализация.
- **Абстракция:** `LibraryItem` (abstract class), интерфейсы `FineStrategy`, `Command`, `ItemObserver`.
- **Abstract class vs interface:** abstract class — общее состояние (id, title...) + частичная реализация; interface — контракт без состояния.
- **Composition over inheritance:** Decorator, Adapter, Strategy используют композицию.

## SOLID (коротко с примерами из проекта)
- **S** — `LibraryService` только правила, `ReportPrinter` только вывод.
- **O** — новая стратегия штрафа/новый тип предмета добавляется без изменения старых классов.
- **L** — любой `FineStrategy`/`LoanPolicy`/`LibraryItem` можно подставить вместо базового.
- **I** — маленькие интерфейсы (`ItemObserver`, `Command`...).
- **D** — Commands и Facade зависят от абстракций/сервиса, Adapter от `CatalogProvider`.

## Что сказать, если спросят «что улучшить»
БД (PostgreSQL + Repository), GUI/REST поверх Facade, реальные email/SMS observers, JUnit + CI.

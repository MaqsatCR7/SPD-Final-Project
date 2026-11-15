# Library Management System

A Java 17+ console application for managing a library catalog, members, loans, and reservations. The project demonstrates nine design patterns and includes 25 automated checks.

## Team

| Student | Name | Design patterns |
| --- | --- | --- |
| A | Максат | Singleton, Factory Method |
| B | Исмайл | Adapter, Decorator |
| C | Бакдаулет | Builder, Facade, Command |

Бакдаулет also contributed the `Loan` model, `LibraryService`, tests, UML diagrams, documentation, and scripts.

## How to run

Requires JDK 17 or later. Run these commands from the project root:

```bash
./compile.sh        # Compile the project
./run.sh            # Run the automatic demo
./run.sh menu       # Open the interactive console menu
./test.sh           # Run all 25 automated checks
```

On Windows, run `run.bat`.

In IntelliJ IDEA, mark `src` as **Sources Root** and `tests` as **Test Sources Root**, then run `library.Main`. Pass `menu` as a program argument to open the interactive menu.

Expected test result: `Passed: 25, Failed: 0`.

## Design patterns

| Pattern | Type | Location | Purpose |
| --- | --- | --- | --- |
| Singleton | Creational | `creational/singleton/LibraryDatabase` | Provides one shared in-memory database. |
| Factory Method | Creational | `creational/factory/` | Creates books, magazines, and DVDs based on `ItemType`. |
| Builder | Creational | `model/Member.Builder` | Creates and validates members with required and optional fields. |
| Adapter | Structural | `structural/adapter/` | Converts legacy catalog records into `LibraryItem` objects. |
| Decorator | Structural | `structural/decorator/` | Adds loan options such as extended duration, SMS reminders, and home delivery. |
| Facade | Structural | `structural/facade/LibraryFacade` | Gives the console UI a single entry point for library operations. |
| Observer | Behavioral | `behavioral/observer/` | Notifies members when reserved items are returned and records events in the audit log. |
| Strategy | Behavioral | `behavioral/strategy/` | Calculates late fees according to Standard, Student, or Premium membership rules. |
| Command | Behavioral | `behavioral/command/` | Encapsulates checkout, return, and reservation operations and supports undo. |

## How the patterns work together

The console UI calls `LibraryFacade`. The facade uses factories to create catalog items, the builder to register members, decorators to add loan options, and commands to run library operations. Commands call `LibraryService`, which uses the shared database and selects the appropriate fine strategy. Changes in item availability trigger observer notifications. The adapter uses the factory to turn legacy catalog records into items.

## SOLID and clean code

- **Single Responsibility** — Models represent library data, the service enforces business rules, and the UI handles console input and output.
- **Open/Closed** — New item creators, fine strategies, and loan decorators can be added through their existing interfaces.
- **Liskov Substitution** — Books, magazines, and DVDs can be used through `LibraryItem`. Fine strategies share the same calculation contract.
- **Interface Segregation** — Focused interfaces such as `ItemObserver`, `FineStrategy`, `Command`, and `CatalogProvider` define specific responsibilities.
- **Dependency Inversion** — Fine calculations use `FineStrategy`, and the catalog adapter accepts a `CatalogProvider`.
- **KISS and DRY** — Enums and polymorphism handle type-specific behavior, while shared logic stays in one place.

## Project documentation

- [Project report](docs/REPORT.md)
- [Defense questions and answers](docs/DEFENSE_QA.md)
- [UML diagrams](uml/)



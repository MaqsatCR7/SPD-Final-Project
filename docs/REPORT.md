# Library Management System
**Final Project Report - Design Patterns**
Astana IT University - Team: <Student A>, <Student B>, <Student C> - Group: <group> - November 2026

---

## 1. Introduction

### 1.1 Problem
Libraries must track items (books, magazines, DVDs), members, loans, reservations and fines. A naive
implementation quickly turns into large `if/else` blocks and tightly coupled classes that are hard to
extend. The goal of this project is to build a **Library Management System** in Java where design
patterns keep the code flexible, readable and easy to extend.

### 1.2 Goals
- Implement at least 6 design patterns (we implemented 9: 3 creational, 3 structural, 3 behavioral).
- Each team member implements at least two patterns individually.
- Apply Clean Code, SOLID, KISS and DRY.
- Provide UML diagrams, screenshots, automatic tests and a Git history of all members.

### 1.3 Technologies
Java 17+, IntelliJ IDEA, Git/GitHub, PlantUML. No external libraries.

---

## 2. Body

### 2.1 Functional requirements
1. Register members (Standard, Student, Premium) with different loan limits (3 / 5 / 10).
2. Add items of different types; import items from a legacy catalog.
3. Check out items with optional extras (extended period, SMS reminder, home delivery).
4. Return items; calculate a fine for late returns depending on the membership.
5. Reserve a borrowed item and get a notification when it becomes available.
6. Undo the last action.
7. Search items, list members, loans and the command history.

### 2.2 Architecture
Layers: **ui** (console menu, demo, printing) -> **Facade** -> **service** (business rules) ->
**database (Singleton)**; **model** contains entities. Patterns are grouped by package:
`creational`, `structural`, `behavioral`.

> Insert here: UML class diagram (`uml/class-diagram.puml` rendered with PlantUML).

### 2.3 Patterns used

| # | Pattern | Type | Problem it solves | Classes | Author |
|---|---------|------|-------------------|---------|--------|
| 1 | Singleton | Creational | Only one shared database | `LibraryDatabase` | A |
| 2 | Factory Method | Creational | Create items without `new` in client code | `ItemCreator`, `BookCreator`, `MagazineCreator`, `DvdCreator`, `ItemFactory` | A |
| 3 | Builder | Creational | Member has many optional fields | `Member.Builder` | A |
| 4 | Adapter | Structural | Legacy catalog has an incompatible text format | `CatalogProvider`, `LegacyCatalogSystem`, `LegacyCatalogAdapter` | B |
| 5 | Decorator | Structural | Combine loan extras without a class for every combination | `LoanPolicy`, `BasicLoanPolicy`, `LoanPolicyDecorator`, 3 decorators | B |
| 6 | Facade | Structural | Simple entry point hiding the subsystems | `LibraryFacade` | B |
| 7 | Observer | Behavioral | Notify members when an item is returned | `ItemSubject`, `ItemObserver`, `Member`, `AuditLogObserver` | C |
| 8 | Strategy | Behavioral | Different fine rules per membership | `FineStrategy`, 3 strategies, `FineCalculator`, `FineStrategyProvider` | C |
| 9 | Command | Behavioral | Undo for checkout / return / reserve | `Command`, 3 commands, `CommandHistory` | C |

#### 2.3.1 Singleton
`LibraryDatabase` uses the initialization-on-demand holder idiom: the private constructor forbids
`new`, and the JVM creates the instance thread-safely when `getInstance()` is first called.
> Insert code: `LibraryDatabase.java`

#### 2.3.2 Factory Method
`ItemFactory` selects an `ItemCreator` from an `EnumMap`. Adding a new item type means adding one
creator class and registering it - existing code is not modified (Open/Closed).
> Insert code: `ItemFactory.java`, `BookCreator.java`

#### 2.3.3 Builder
`Member.Builder` has required parameters (name, e-mail) in its constructor and optional ones as
chained methods; `build()` validates the data and the Member constructor is private.
> Insert code: `Member.java`

#### 2.3.4 Adapter
`LegacyCatalogAdapter` implements our `CatalogProvider` interface and translates the text records
`TYPE|ID|TITLE|CREATOR|YEAR` of `LegacyCatalogSystem` into `LibraryItem` objects; invalid records are skipped.
> Insert code: `LegacyCatalogAdapter.java`

#### 2.3.5 Decorator
`BasicLoanPolicy` is wrapped by any combination of `ExtendedLoanDecorator`, `SmsReminderDecorator`
and `HomeDeliveryDecorator`; each decorator adds days, fee and description.
> Insert code: `LoanPolicyDecorator.java`, `ExtendedLoanDecorator.java`

#### 2.3.6 Facade
`LibraryFacade` hides the database, factory, builder, decorators, commands and service. The UI uses only this class.
> Insert code: `LibraryFacade.java`

#### 2.3.7 Observer
`LibraryItem` is the subject. A member who reserves a borrowed item subscribes to it and receives a
notification when `setAvailable(true)` is called. `AuditLogObserver` logs every state change.
> Insert code: `LibraryItem.java` (subscribe / notifyObservers), `Member.update`

#### 2.3.8 Strategy
`FineCalculator` (context) delegates to `StandardFineStrategy`, `StudentFineStrategy` or
`PremiumFineStrategy`. The strategy is chosen by the membership type, so there are no `if/else` chains.
> Insert code: `FineStrategy.java`, `FineCalculator.java`

#### 2.3.9 Command
Every action is an object with `execute()` and `undo()`. `CommandHistory` keeps a stack, which gives a
reliable undo and a log of operations.
> Insert code: `CheckoutCommand.java`, `CommandHistory.java`

### 2.4 How patterns work together
`LibraryFacade.checkout()` -> builds a `LoanPolicy` (Decorator) -> wraps the action in a `CheckoutCommand`
(Command) -> `LibraryService` uses the `LibraryDatabase` (Singleton) -> `item.setAvailable(false)` informs
observers (Observer). On return, `LibraryService` picks a Strategy for the fine. Imported items are
created with the Factory through the Adapter.

### 2.5 SOLID, KISS, DRY
See the table in `README.md`: SRP (separate model / service / ui / facade), OCP (new creators, strategies,
decorators), LSP, ISP (small interfaces), DIP (depend on interfaces). KISS - simple in-memory storage and
clear class names. DRY - one id generator, one printer, one fine calculator.

### 2.6 Testing
`LibraryTests` contains 25 automatic checks (`./test.sh`): every pattern plus business rules
(loan limit, double return, unknown member, undo, fines).

> Insert screenshots: (1) demo output, (2) interactive menu, (3) tests passed, (4) Git history.

---

## 3. Conclusion
We built a working Library Management System that uses 9 design patterns (3 creational, 3 structural,
3 behavioral) which cooperate in one project. The patterns removed duplication and `if/else` chains, made
the system easy to extend (new item types, fine rules, loan options) and testable. Each team member
implemented three patterns and contributed commits to the shared Git repository.

## 4. Further work
- Persistent storage (PostgreSQL / JDBC) behind a Repository interface.
- GUI or web interface (REST API) on top of the Facade.
- E-mail / SMS observers that really send messages.
- Search with filters, item ratings and a recommendation Strategy.
- Roles (librarian / member) and authentication.
- Unit tests with JUnit and a CI pipeline.

## 5. References
- Gamma E. et al. *Design Patterns: Elements of Reusable Object-Oriented Software*.
- Freeman E. et al. *Head First Design Patterns*.
- Martin R. C. *Clean Code*.
- refactoring.guru - Design Patterns catalog.

# Library Management System (Java 17+)


## How to run
```
./compile.sh        # build  (Windows: run.bat)
./run.sh            # automatic demo of all patterns
./run.sh menu       # interactive console menu
./test.sh           # 25 automatic checks
```
Or in IntelliJ IDEA: mark `src` as Sources Root, `tests` as Test Sources Root, run `library.Main`
(program argument `menu` for the interactive mode).

## Patterns (9 - three of each type)

| # | Pattern | Type | Where | What it does in the library |
|---|---------|------|-------|-----------------------------|
| 1 | Singleton | Creational | `creational/singleton/LibraryDatabase` | one shared in-memory database |
| 2 | Factory Method | Creational | `creational/factory/*` | creates Book / Magazine / DVD by `ItemType` |
| 3 | Builder | Creational | `model/Member.Builder` | builds a Member (required + optional fields, validation) |
| 4 | Adapter | Structural | `structural/adapter/*` | imports the legacy text catalog into our `LibraryItem`s |
| 5 | Decorator | Structural | `structural/decorator/*` | loan extras: Extended, SMS reminder, Home delivery |
| 6 | Facade | Structural | `structural/facade/LibraryFacade` | single simple API for the UI |
| 7 | Observer | Behavioral | `behavioral/observer/*` | members get notified when a reserved item is returned; audit log |
| 8 | Strategy | Behavioral | `behavioral/strategy/*` | fine calculation per membership (Standard / Student / Premium) |
| 9 | Command | Behavioral | `behavioral/command/*` | checkout / return / reserve with **undo** |

Patterns cooperate: Facade -> Factory/Builder/Decorator/Command; Command -> Service -> Singleton + Strategy;
Service -> Item.setAvailable -> Observer; Adapter -> Factory.

## Suggested work split (3 students, each >= 2 patterns, 3 here)

| Student | Patterns | Files |
|---------|----------|-------|
| Student A | Singleton, Factory Method, Builder | `creational/*`, `model/*` |
| Student B | Adapter, Decorator, Facade | `structural/*`, `ui/*` |
| Student C | Observer, Strategy, Command | `behavioral/*`, `service/*`, `tests/*` |

For 2 students: A takes Singleton/Factory/Builder/Adapter, B takes Decorator/Facade/Observer/Strategy/Command.

## SOLID / clean code
- **S**: model, service (rules), ui (printing), facade (entry point), each class has one job.
- **O**: new item type = new `ItemCreator`; new fine rule = new `FineStrategy`; new loan option = new decorator.
- **L**: Book / Magazine / Dvd and all strategies/decorators are interchangeable through their interfaces.
- **I**: small interfaces (`ItemObserver`, `ItemSubject`, `FineStrategy`, `Command`, `CatalogProvider`).
- **D**: Commands depend on `LibraryService`; the UI depends on the Facade; Adapter depends on `CatalogProvider`.
- **KISS / DRY**: no if/else chains for types (maps/enums + polymorphism), ids and printing are in one place each.

## Git IMPORTANT - contribution check, -5% penalty otherwise
Every team member must make their own commits from their own account:
```
git init
git add .
git commit -m "Initial structure"
git remote add origin <repo-url>
git push -u origin main
```
Each student commits and pushes the files of their patterns (see table above) with meaningful messages.

## Submission checklist (deadline Mon 15 Nov 2026, 23:59, each student submits!)
- [ ] Report PDF (`docs/REPORT.md` -> add screenshots + UML image -> export to PDF)
- [ ] Source code zip
- [ ] Git repo with commits of ALL members
- [ ] At least 6 patterns, 2 per student, 1 of each type

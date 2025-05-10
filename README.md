# Sistema di Gestione Biblioteca

Questo progetto implementa un sistema di gestione per una biblioteca, consentendo la gestione di libri, riviste e collezioni di media.

## Caratteristiche

- Gestione di libri e riviste
- Creazione di collezioni di media
- Ricerca per titolo, autore e anno di pubblicazione
- Persistenza dei dati su file
- Interfaccia utente a console

## Pattern di Design Implementati

- **Factory Pattern**: Utilizzato nella classe `MediaFactory` per la creazione di oggetti media
- **Composite Pattern**: Implementato attraverso l'interfaccia `Media` e le classi `Book`, `Magazine` e `MediaCollection`
- **Iterator Pattern**: Implementato attraverso l'interfaccia `MediaIterator` e la classe `MediaCollectionIterator`
- **Singleton Pattern**: Utilizzato nelle classi `MediaRepository`, `MediaService` e `FileStorageManager`
- **Exception Shielding**: Implementato attraverso la gerarchia di eccezioni personalizzate

## Tecnologie Core Utilizzate

- **Collections Framework**: Utilizzato per la gestione delle collezioni di media
- **Generics**: Utilizzati nell'interfaccia `Repository` e nelle implementazioni
- **Java I/O**: Utilizzato per la persistenza dei dati su file
- **Logging**: Implementato attraverso la classe `LoggerManager`

## Struttura del Progetto

- **model**: Contiene le classi del modello dati
- **repository**: Contiene le classi per la persistenza dei dati
- **service**: Contiene i servizi per la logica di business
- **factory**: Contiene le factory per la creazione degli oggetti
- **iterator**: Contiene gli iteratori per le collezioni
- **exception**: Contiene le eccezioni personalizzate
- **ui**: Contiene l'interfaccia utente a console
- **util**: Contiene le classi di utilità
- **test**: Contiene le classi di test

## Come Eseguire

Per eseguire l'applicazione:

```bash
java -cp target/classes com.library.Main
```

Per eseguire i test:

```bash
java -cp target/classes com.library.test.MediaServiceTest
```

## Requisiti

- Java 8 o superiore
- Maven (per la compilazione)

## Compilazione

Per compilare il progetto:

```bash
mvn clean compile
```

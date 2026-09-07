# NeptuneSystem Modules

Repository contenente i moduli di **business logic** utilizzati dal sistema NeptuneSystem.

I moduli sono sviluppati come progetti Maven indipendenti e vengono distribuiti tramite **GitHub Packages**, permettendo alle applicazioni NeptuneSystem di importarli come normali dipendenze Maven.

> **Nota:** i plugin NeptuneSystem sono gestiti in una repository separata e non fanno parte di questo progetto.

---

## Repository

**GitHub:** [ATDSviluppo/atd-neptunesystem-modules](https://github.com/ATDSviluppo/atd-neptunesystem-modules)

**Maven Package Registry:**

```text
https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
```

---

# Architecture

La repository è organizzata come progetto Maven multi-module.

```text
atd-neptunesystem-modules/
│
├── AuthenticationModule/
├── CommonModule/
├── HMIModule/
├── HardwareManagerModule/
├── MainBusinessLogicDpiModule/
├── MainBusinessLogicKeyModule/
├── MobileModule/
├── SafetyModule/
├── ZCarFleetModule/
│
├── .github/
│   └── workflows/
│
├── pom.xml
├── README.md
└── UpdateNeptuneModules.bat
```

Ogni directory rappresenta un artefatto Maven indipendente.

Il `pom.xml` nella root ha funzione di **aggregator** e permette di compilare i moduli attraverso un unico comando Maven.

---

# Modules

| Module                       | Descrizione                                                               |
| ---------------------------- | -----------------------------------------------------------               |
| `CommonModule`               | Componenti e funzionalità condivise dagli altri moduli                    |
| `AuthenticationModule`       | Gestione dell'autenticazione e delle funzionalità correlate               |
| `HMIModule`                  | Funzionalità relative all'interfaccia HMI neptune system                  |
| `HardwareManagerModule`      | Gestione e comunicazione con l'hardware (S2 - 900 - MDS)                  |
| `MainBusinessLogicDpiModule` | Business logic relativa alla distribuzione di DPI                         |
| `MainBusinessLogicKeyModule` | Business logic relativa alla distribuzione di chiavi                      |
| `MobileModule`               | Funzionalità utilizzate dal sistema mobile Neptune                        |
| `SafetyModule`               | Funzionalità relative alla comunicazione con il gestionale Safety         |
| `ZCarFleetModule`            | Funzionalità relative alla comunicazione con il gestionale ZCarFleet      |

I plugin non vengono distribuiti tramite questa repository.

---

# Maven Project Structure

Il `pom.xml` root definisce i moduli che fanno parte del reactor Maven:

```xml
<modules>
    <module>AuthenticationModule</module>
    <module>CommonModule</module>
    <module>HMIModule</module>
    <module>HardwareManagerModule</module>
    <module>MainBusinessLogicDpiModule</module>
    <module>MainBusinessLogicKeyModule</module>
    <module>MobileModule</module>
    <module>SafetyModule</module>
    <module>ZCarFleetModule</module>
</modules>
```

Maven determina automaticamente l'ordine di compilazione sulla base delle dipendenze tra i moduli.

Ad esempio:

```text
CommonModule
      │
      ├──────────────┐
      ▼              ▼
Authentication     HardwareManager
      │              │
      └──────┬───────┘
             ▼
   MainBusinessLogic
```

L'ordine fisico dei `<module>` nel `pom.xml` non deve quindi essere utilizzato per gestire manualmente le dipendenze.

---

# Module Dependencies

I moduli possono dipendere dagli altri artefatti pubblicati su GitHub Packages.

Esempio:

```xml
<dependency>
    <groupId>com.CommonModule</groupId>
    <artifactId>commonmodule</artifactId>
    <version>1.0.0</version>
</dependency>
```

Le coordinate Maven di un modulo sono:

```text
groupId:artifactId:version
```

Ad esempio:

```text
com.CommonModule:commonmodule:1.0.0
```

## Dependency direction

Le dipendenze devono seguire una direzione coerente.

Un modulo non deve introdurre una dipendenza circolare con un altro modulo.

Esempio da evitare:

```text
AuthenticationModule
        │
        ▼
    HMIModule
        │
        ▼
AuthenticationModule
```

Questo genera un errore Maven:

```text
ProjectCycleException
```

Quando possibile, le funzionalità condivise devono essere spostate in `CommonModule` invece di creare dipendenze circolari.

---

# GitHub Packages

Gli artefatti vengono pubblicati nel GitHub Maven Package Registry:

```text
https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
```

Ogni modulo pubblicabile deve avere un `distributionManagement` simile al seguente:

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>
            https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
        </url>
    </repository>
</distributionManagement>
```

Per utilizzare gli artefatti come dipendenze:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>
            https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
        </url>
    </repository>
</repositories>
```

---

# Autenticazione

GitHub Packages richiede autenticazione per l'accesso ai package.

Quando i moduli vengono utilizzati all'interno di GitHub Actions, è possibile utilizzare il token automatico:

```yaml
${{ secrets.GITHUB_TOKEN }}
```

Il workflow deve avere i permessi necessari.

Per la lettura dei package:

```yaml
permissions:
  contents: read
  packages: read
```

Per la pubblicazione:

```yaml
permissions:
  contents: read
  packages: write
```

Con `actions/setup-java` è possibile configurare automaticamente le credenziali Maven:

```yaml
- name: Setup Java
  uses: actions/setup-java@v4
  with:
    distribution: temurin
    java-version: '21'
    server-id: github
    server-username: GITHUB_ACTOR
    server-password: GITHUB_TOKEN
```

---

# Pubblicazione

La pubblicazione viene gestita tramite GitHub Actions.

Il workflow può essere eseguito:

* manualmente tramite `workflow_dispatch`;
* automaticamente quando vengono modificati determinati moduli sul branch principale.

In caso di modifica di un singolo modulo, il workflow può individuare il modulo interessato e pubblicare solamente quello.

Esempio:

```text
Push
 │
 ▼
Detect changed modules
 │
 ├── AuthenticationModule
 │
 ▼
Build
 │
 ▼
Test
 │
 ▼
Deploy Maven artifact
 │
 ▼
GitHub Packages
```

Per un'esecuzione manuale è invece possibile pubblicare tutti i moduli presenti nel progetto.

---

# Building Locale

Per compilare tutti i moduli dalla root:

```bash
mvn clean install
```

Per compilare senza eseguire i test:

```bash
mvn clean install -Dmaven.test.skip=true
```

Per effettuare solamente il package:

```bash
mvn clean package
```

---

# Building a Single Module

È possibile compilare un singolo modulo direttamente dalla sua directory:

```bash
cd AuthenticationModule
mvn clean install
```

Oppure dalla root utilizzando il reactor Maven:

```bash
mvn clean install -pl AuthenticationModule
```

Se il modulo richiede anche la compilazione delle proprie dipendenze:

```bash
mvn clean install -pl AuthenticationModule -am
```

Dove:

```text
-pl
```

seleziona il progetto.

```text
-am
```

include anche i moduli necessari.

---

# Publishing a Module Locally

Per installare un artefatto nel repository Maven locale:

```bash
mvn clean install
```

L'artefatto sarà disponibile nella repository Maven locale:

```text
~/.m2/repository/
```

---

# Versioning

I moduli utilizzano versioni Maven esplicite.

Esempio:

```xml
<version>1.0.0</version>
```

Quando viene pubblicata una nuova versione di un modulo, deve essere incrementata la versione:

```text
1.0.0
1.0.1
1.1.0
2.0.0
```

Una volta pubblicata una release, non è consigliabile riutilizzare la stessa versione per un contenuto differente.

Ad esempio, dopo aver pubblicato:

```text
commonmodule:1.0.0
```

una modifica al codice dovrebbe generare una nuova versione:

```text
commonmodule:1.0.1
```

anziché ripubblicare `1.0.0`.

---

# Maven Artifact Naming

Gli `artifactId` utilizzati per GitHub Packages devono essere scritti in lowercase.

Esempio corretto:

```xml
<artifactId>commonmodule</artifactId>
```

Esempio da evitare:

```xml
<artifactId>CommonModule</artifactId>
```

Una configurazione non conforme può causare un errore GitHub Packages:

```text
422 Unprocessable Entity
```

Il `groupId` può invece mantenere la convenzione attualmente utilizzata dal progetto, ad esempio:

```xml
<groupId>com.CommonModule</groupId>
```

---

# Creare un nuovo modulo

Per aggiungere un nuovo modulo:

### 1. Creare il nuovo progetto maven

```text
NewModule/
```

### 2. Creare il `pom.xml`

Il modulo deve avere coordinate Maven proprie:

```xml
<groupId>com.NewModule</groupId>
<artifactId>newmodule</artifactId>
<version>1.0.0</version>
```

### 3. Aggiungerlo al POM root

```xml
<modules>
    ...
    <module>NewModule</module>
</modules>
```

### 4. Configurare GitHub Packages

Aggiungere:

```xml
<repositories>
    <repository>
        <id>github</id>
        <url>
            https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
        </url>
    </repository>
</repositories>
```

e:

```xml
<distributionManagement>
    <repository>
        <id>github</id>
        <name>GitHub Packages</name>
        <url>
            https://maven.pkg.github.com/ATDSviluppo/atd-neptunesystem-modules
        </url>
    </repository>
</distributionManagement>
```

### 5. Configurare le dipendenze

Aggiungere solamente le dipendenze effettivamente necessarie.

### 6. Verificare il reactor

```bash
mvn validate
```

e successivamente:

```bash
mvn clean install
```

---

# Aggiornamento di un modulo

Quando viene modificato un modulo:

1. aggiornare il codice;
2. verificare le dipendenze;
3. incrementare la versione se necessario;
4. eseguire la build;
5. verificare i test;
6. pubblicare il nuovo artefatto su GitHub Packages.

Esempio:

```bash
mvn clean install
```

seguito dalla pubblicazione tramite GitHub Actions.

---

# Troubleshooting

## 409 Conflict

Errore tipico:

```text
409 Conflict
```

Può verificarsi quando si tenta di pubblicare nuovamente un artefatto con una versione già presente.

Esempio:

```text
commonmodule:1.0.0
```

Se `1.0.0` è già pubblicato, utilizzare una nuova versione.

---

## 422 Unprocessable Entity

Errore tipico:

```text
422 Unprocessable Entity
```

Controllare innanzitutto l'`artifactId`.

Deve essere lowercase:

```xml
<artifactId>mainbusinesslogicdpimodule</artifactId>
```

e non:

```xml
<artifactId>MainBusinessLogicDpiModule</artifactId>
```

---

## Could not find artifact

Errore:

```text
Could not find artifact
```

Verificare:

* `groupId`;
* `artifactId`;
* `version`;
* URL GitHub Packages;
* autenticazione;
* disponibilità dell'artefatto nel package registry.

Esempio:

```text
com.CommonModule:commonmodule:1.0.0
```

deve corrispondere esattamente alle coordinate dell'artefatto pubblicato.

---

## ProjectCycleException

Errore:

```text
ProjectCycleException
```

indica una dipendenza circolare tra moduli.

Esempio:

```text
Module A → Module B → Module A
```

La soluzione è rimuovere la dipendenza non necessaria oppure spostare il codice condiviso in un modulo comune.

---

# UpdateNeptuneModules

Il repository contiene anche:

```text
UpdateNeptuneModules.bat
```

utilizzato per aggiornare localmente i moduli NeptuneSystem secondo la configurazione prevista dal progetto.

---

# Development Guidelines

Quando si aggiungono o modificano moduli:

* mantenere i moduli il più possibile indipendenti;
* evitare dipendenze circolari;
* utilizzare `CommonModule` per funzionalità realmente condivise;
* non introdurre dipendenze verso i plugin;
* mantenere coerenti `groupId`, `artifactId` e `version`;
* utilizzare `artifactId` lowercase;
* incrementare la versione quando viene pubblicato un nuovo artefatto;
* non riutilizzare una versione già pubblicata per contenuti differenti.

---

# Summary

`atd-neptunesystem-modules` rappresenta il repository Maven dei **moduli core NeptuneSystem**.

La repository è responsabile di:

* sviluppo dei moduli di business logic;
* gestione delle dipendenze tra moduli;
* build Maven;
* versioning degli artefatti;
* pubblicazione su GitHub Packages;
* integrazione con le applicazioni NeptuneSystem tramite dipendenze Maven.

I **plugin** sono deliberatamente esclusi e vengono gestiti in una repository separata.

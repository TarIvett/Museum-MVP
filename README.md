# Museum Management System (Arhitectură Model-View-Presenter)

## Descriere generală

Acest proiect reprezintă un sistem de gestiune pentru un muzeu, utilizat pentru administrarea artiștilor și operelor de artă expuse.

Aplicația este construită pe o arhitectură **Model-View-Presenter (MVP)** strictă, punând accent pe decuplarea logicii de afaceri de interfața grafică pentru a asigura modularitate și scalabilitate.

Interfața este optimizată pentru o experiență de tip **„mobile app”**, având o rezoluție de **360 × 640 pixeli** și o tematică **Dark Mode cu accente aurii**.

---

## Enunțul problemei

Sistemul a fost conceput pentru a automatiza fluxurile de lucru ale administratorilor sau curatorilor de muzeu, permițând:

- **Gestiunea Artiștilor**
  - Adăugarea, ștergerea și actualizarea datelor

- **Vizualizare Detaliată**
  - Afișarea biografiilor complete (nume, data/locul nașterii, naționalitate)
  - Lista de opere realizate

- **Căutare și Filtrare**
  - Căutarea artiștilor după nume
  - Căutarea operelor după titlu sau tip (peisaj, portret etc.)

- **Gestiunea Operelor (CRUD)**
  - Administrarea pieselor expuse
  - Fiecare operă poate conține între 1 și 3 imagini

- **Sistem Media**
  - Încărcarea și redarea asincronă a imaginilor
  - Procesare multithreading

---

## Arhitectura sistemului (Strict MVP)

Aplicația respectă principiile **SOLID** și utilizează șablonul **MVP** pentru a izola responsabilitățile:

### 1. Model
- Gestionează entitățile sistemului (**Artist**, **Artwork**)
- Abstractizează conexiunea SQL prin clase de tip Repository (DAO)
- Stochează imaginile în sistemul de fișiere local (`uploaded_images/`)
- În baza de date MySQL sunt păstrate doar căile relative (performanță mai bună)

---

### 2. View (Passive View)
- Implementat folosind **JavaFX 23 (FXML și CSS)**
- Clasele sunt „stateless” și lipsite de logică
- Acționează ca executanți ai Presenter-ului
- Nu realizează validări sau conversii de date

---

### 3. Presenter
- Reprezintă nucleul logic al aplicației
- Comunică cu View-ul doar prin interfețe contractuale:
  - `IArtistGUI`
  - `IArtworkGUI`
- Gestionează navigarea între ecrane prin routerul `MuseumGUI`

---

## Stack Tehnologic

- **Limbaj:** Java 23 (JDK 23)
- **UI Framework:** JavaFX 23.0.1
- **Bază de Date:** MySQL 8.0
- **Modelare:** StarUML
  - Use Case Diagram
  - Activity Diagram
  - Class Diagram
  - ER Diagram
  - Package Diagram
- **IDE:** IntelliJ IDEA

---

## Detalii Implementare API & Servicii

În arhitectura actuală monolitică, comunicarea între componente se face prin apeluri de metodă intermediate de interfețe.

Sistemul este pregătit pentru scalare datorită decuplării stricte:

- **User / Artist Management**
  - `ArtistRepository`
  - `ArtistPresenter`

- **Artwork Management**
  - `ArtworkRepository`
  - `ArtworkPresenter`

- **I/O Operations**
  - Utilizarea `FileChooser`
  - Copiere sigură a resurselor media în directorul local al proiectului

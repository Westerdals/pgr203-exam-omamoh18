# Oppgave for kontinuasjonseksamen i Avansert Java (PGR203)

* **Emnekode**: PGR203
* **Emnenavn**: Avansert Java
* **Vurderingskombinasjon**: Funksjonalitet og kodekvalitet i fungerende program
* **Utleveringsfrist**: 
* **Innleveringsfrist**: 
* **Filformat**: ZIP-fil fra Github classroom med Java-kildekode og filnavn som matcher deres github-repository.

### Innlevering

* Oppgaven **skal** løses i Github vha Github Classroom med link https://classroom.github.com/a/KEdk-QcU. Repository på github **skal** være private
* Oppgaven **skal** leveres i *Wiseflow* som en ZIP-fil med kopi fra Github Classroom
* Oppgaven **bør** løses parvis. Dere kan velge om dere vil beholde par fra undervisningen eller finne nye partnere. Ønsker du å levere i gruppe på tre må dette avklares med foreleser
* Innleveringen skal deles med en annen gruppe for gjensidig tilbakemelding. Tilbakemelding skal gis i form av Github issues
* README.md på Github **skal** linke til Travis-CI som skal kjøre enhetstester uten feil. README-filen skal også inneholde link til gitt tilbakemelding til annet team, et UML-diagram samt beskrivelse av hva kandiditene ønskes skal vurderes i evalueringen av innleveringen
* Koden **skal** lese database settings fra en fil som heter `pgr203.properties` og ser ut som følger:

```properties
dataSource.url=...
dataSource.username=...
dataSource.password=...
```

### Oppgave

Mappeoppgaven for PGR203 er å lage en backend server for å håndtere spillet [Yatzy](https://no.wikipedia.org/wiki/Yatzy). Serveren skal la bruker kunne registerer spillere og kategorier for et spill, legge inn kast og beregne poengene til hver spiller.

Programmet skal utvikles på en måte som demonstrerer programmeringsferdigheter slik det vises i undervisningen. Spesielt skal all funksjonalitet ha automatiske tester og være fri for grunnleggende sikkerhetssvakheter. Programmet skal demonstre at kandidatene mestrer Sockets og JDBC bibliotekene i Java.

Programmet skal leveres i form av et maven prosjekt som kan bygge en `executable jar` og lagre data i en PostgreSQL-database.

Dere skal levere en webserver som skal kunne brukes i nettleseren for å legge inn og oppdatere et spill.

Funksjonaliteten bør inkludere:
* Opprett et nytt _Spill_ med _beskrivelse_ og _dato_
* Liste opp eksisterende spill og vise detaljer for hvert spill
* Velge hvilke _Kategorier_ som skal inngå i spillet, fra beskrivelsen av [Yatzy på wikipedia](https://no.wikipedia.org/wiki/Yatzy) (enere, toere, treere, firere, femmere, seksere, ett par, to par, tre like, etc)
* Registrere en eller flere _Spillere_ som skal delta i spillet, med _navn_ og _alder_ på hver spiller
* Legge inn et Terningkast (for eksempel "1,1,2,6,6") på en Kategori for en Spiller
* Beregne poengsum for hver spiller og presentere hvilken spiller som er vinner

Prosjektet må følge god programmeringsskikk. Dette er viktige krav og feil på et enkelt punkt kan gi en hel karakter i trekk.
* Koden skal utvikles på Git, med Maven og kjøre tester på Travis-CI
* Koden skal leverer med god testdekning
* Det skal ikke forekomme SQL Injection feil
* Databasepassord skal være konfigurert i en fil som _ikke_ sjekkes inn i git
* README-fil må dokumentere hvordan man bygger, konfigurerer og kjører løsningen
* README-fil må dokumentere designet på løsningen
* Koden bør abstrahere ut felles kode i controllere for å unngå duplisering
* Koden bør ha god test-dekning av spesielt DAO-kode

**For å få A må alle de funksjonelle kravene være løst og leveransen må følge alle kravene til god programmeringsskikk. I tillegg må man ha minst 2 ekstrapoeng fra listen over mulighet for ekstrapoeng**

Brukervennlighet *er **ikke*** et vurderingskriterie for karakteren.

## Vedlegg: Eksempel på frontend kode

### `index.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>View existing</h1>

<div id="data"></div>

<div>
    <a href="create.html">Add new ...</a>
</div>
</body>
<script>
fetch("/api/...")
    .then(function(response) {
        return response.text();
    }).then(function(text) {
        document.getElementById("data").innerHTML = text;
    });
</script>
</html>
```

### `create.html`

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <link rel="stylesheet" href="style.css">
</head>
<body>
<h1>Add ...</h1>

<form method="POST" action="/api/...">
    <label>Field 1: <input type="text" name="..." /></label>
    <label>Field 2: <input type="text" name="..." /></label>
    <button>Add</button>
</form>

<div>
    <a href=".">Return to front page</a>
</div>
</body>
</html>
```

### `style.css`

```css
body {
    background-color: lightblue;
}
```

## Vedlegg: Sjekkliste for innlevering

* [x] Dere har lastet opp en ZIP-fil med navn basert på navnet på deres Github repository
* [x] Koden er sjekket inn på github.com/Westerdals-repository
* [x] GitHub repository er private, men delt med gruppen dere gjør gjensidig tilbakemelding til
* [x] Dere har mottatt og gitt minst 2 positive og 2 korrektive GitHub issues i github repository fra en annen gruppe
* [x] Dere har committed kode med begge prosjektdeltagernes GitHub konto (alternativt: README beskriver arbeidsform)

### README.md

* [x] `README.md` inneholder en korrekt link til Travis CI
* [x] `README.md` beskriver prosjektets funksjonalitet, hvordan man bygger det og hvordan man kjører det 
* [x] `README.md` beskriver eventuell ekstra leveranse utover minimum
* [ ] `README.md` inneholder link til en diagram som viser datamodellen
* [x] `README.md` inneholder link til gruppen dere har gitt tilbakemelding til

### Koden

* [x] `mvn package` bygger en executable jar-fil
* [x] Koden inneholder et godt sett med tester
* [x] `java -jar target/...jar` (etter `mvn package`) lar bruker legge til og liste ut data fra databasen via webgrensesnitt
* [x] Programmet leser `dataSource.url`, `dataSource.username` og `dataSource.password` fra `pgr203.properties` for å connecte til databasen
* [x] Programmet bruker Flywaydb for å sette opp databaseskjema
* [x] Server skriver nyttige loggmeldinger, inkludert informasjon om hvilken URL den kjører på ved oppstart

### Funksjonalitet

* [x] Programmet lar brukeren opprette nye spill
* [x] Programmet lar brukeren liste eksisterende spill
* [x] Programmet lar brukeren legge til spillere på et spill
* [x] Programmet lar brukeren velge hvilke kategorier som skal inngå i et spil
* [x] Programmet lar brukeren registrere kast på en kategori for en spiller
* [x] Programmer beregner poengscore for brukerne

## Vedlegg: Mulighet for ekstrapoeng

* [x] Avansert datamodell (mer enn 3 tabeller)
* [x] Avansert funksjonalitet (redigering av navn på spill, fjerning eller endring av spillere, definere nye kategorier, støtte for varianter av Yatzy som Maxi-Yatzy eller joker-terning)
* [ ] Implementasjon av cookies for å konstruere sesjoner
* [x] UML diagram som dokumenterer datamodell og/eller arkitektur (presentert i README.md)
* [ ] Rammeverk rundt Http-håndtering (AbstractHttpController eller en god HttpMessage class med HttpRequest og HttpResponse subtyper) som gjenspeiler RFC7230
* [x] God bruk av DAO-pattern
* [x] Korrekt håndtering av norske tegn i HTTP
* [ ] Link til video med god demonstrasjon av ping-pong programmering
* [ ] Annet

# AI City Climate Command — Java Backend

A complete, working prototype: your existing dashboard (`index.html`) now
talks to a real Java (Spring Boot) backend, backed by a real database (H2,
file-based, so data survives restarts).

## What's inside

```
ai-city-climate/
├── pom.xml
├── src/main/java/com/aicity/climate/
│   ├── ClimateApplication.java        <- main class, run this
│   ├── model/ClimateLog.java          <- database entity (table = climate_log)
│   ├── model/ClimateLogRequest.java   <- incoming POST body shape
│   ├── repository/ClimateLogRepository.java
│   ├── controller/ClimateController.java  <- REST API
│   └── config/WebConfig.java          <- CORS, so the page works from anywhere
└── src/main/resources/
    ├── application.properties         <- DB config, port 8080
    └── static/index.html              <- your dashboard, served automatically
```

## How to run it in IntelliJ IDEA

1. **Open the project**
   `File > Open...` and select the `ai-city-climate` folder (the one with
   `pom.xml` in it). IntelliJ will detect it as a Maven project and download
   the dependencies automatically (needs internet the first time).

2. **Run it**
   Open `ClimateApplication.java` and click the green ▶ run icon next to the
   `main` method (or right-click the file → **Run 'ClimateApplication'**).

   You should see Spring Boot's startup banner in the console, ending with
   something like `Started ClimateApplication in X seconds` and
   `Tomcat started on port 8080`.

3. **Open the dashboard**
   Go to **http://localhost:8080** in your browser. That's it — the page is
   served directly by the Java app, and its buttons already call the real
   API.

## What it does

- **Seed Data** button → `GET /api/climate/init-demo` → inserts 5 demo rows
  into the database (only if it's currently empty, so it won't duplicate).
- **Optimize Route / Scan Settlements / Scan Disinformation** buttons →
  `POST /api/climate/log` → inserts a new row.
- The table loads from `GET /api/climate/logs`, newest first.
- Every row is a real row in an H2 database file at `./data/climate-db.mv.db`
  inside the project folder — close the app, reopen it, the data is still
  there.

## Real-time streaming (WebSocket)

The dashboard is now fully live — you don't need to click any button for new
rows to appear:

- The page opens a WebSocket connection to `ws://localhost:8080/ws`
  (SockJS + STOMP) and subscribes to `/topic/logs`.
- A background job (`ClimateEventSimulator`, annotated `@Scheduled`) creates
  one simulated event every 15 seconds and pushes it to every connected
  browser tab instantly — no polling.
- The green/yellow/red dot next to "Live Database Log" shows connection
  status (connecting → live → disconnected, with automatic reconnect).
- Manual button clicks go through the same broadcast path
  (`ClimateLogService.saveAndBroadcast`), so they show up instantly too, in
  every open tab, not just the one you clicked in.

To change how often the simulator fires, edit the `fixedRate` value (in
milliseconds) in `ClimateEventSimulator.java`. To plug in a real data feed
later, just call `climateLogService.saveAndBroadcast(...)` from wherever
your real sensor/API data comes in — the WebSocket push is already wired.

## Inspecting the database directly

With the app running, visit **http://localhost:8080/h2-console** and log in
with:
- JDBC URL: `jdbc:h2:file:./data/climate-db`
- User: `sa`
- Password: *(leave blank)*

You can then run SQL directly, e.g. `SELECT * FROM CLIMATE_LOG;`.

## Switching to MySQL/PostgreSQL later (optional)

The code uses standard Spring Data JPA, so swapping the database is just a
matter of changing `pom.xml` (swap the `h2` dependency for
`mysql-connector-j` or `postgresql`) and updating the four
`spring.datasource.*` lines in `application.properties` to point at your
server. No Java code changes needed.

## API reference

| Method | Path                    | Body                                                    | Description                    |
|--------|-------------------------|----------------------------------------------------------|--------------------------------|
| GET    | `/api/climate/logs`     | –                                                        | List all logs, newest first    |
| POST   | `/api/climate/log`      | `{ category, title, description, status }`               | Insert a new log entry         |
| GET    | `/api/climate/init-demo`| –                                                        | Seed demo data (once)          |
| DELETE | `/api/climate/logs`     | –                                                        | Clear all logs                 |

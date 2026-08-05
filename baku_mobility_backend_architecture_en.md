# Baku Smart Mobility System

**Full Project Document**

*Business Plan · Technical Architecture · Technology Stack · Development Roadmap*

**Scope:** Entire city of Baku
**Phase 1:** 1 month (2 people + AI-assisted development)
**Phase 2+:** Team expansion

*The first step toward Baku's mobility ecosystem, with live traffic intelligence, forecasting, and anomaly detection*

## Table of Contents

1. Executive Summary
2. Problem & Market Need
3. Vision: Baku Urban Mobility Ecosystem
4. Business Plan
5. Product Core & Features
6. Team Structure & Roles
7. Technical Architecture
8. Technology Stack (Tools)
9. Data Strategy
10. Development Roadmap
11. Risks & Risk Management
12. Success Metrics (KPI)
13. Long-Term Ecosystem Vision
14. Conclusion

---

## 1. Executive Summary

The Baku Smart Mobility System is a predictive traffic intelligence platform covering Baku's entire arterial road network, built on historical behavior models. Unlike existing map applications (Yandex, Google Maps), the system does not just show the "current" state — it forecasts future traffic conditions and detects unusual events (accidents, events, road closures) in real time.

The project is carried out in two phases. In Phase 1 (1 month), a 2-person core team (Backend + AI/Data Engineer) brings the core engine — APIs, the prediction model, an AI-assisted dashboard — to a fully working state. In Phase 2, the team expands to add a mobile app, full UI/UX, and production-level infrastructure.

In the long term, this product serves as the foundation of a broader Urban Mobility Ecosystem for Baku — public transport integration, micro-mobility, and an open data platform.

> **Core value proposition**
> "Yandex/Google show you the current situation. We build a historical behavior model for every arterial road in Baku and predict the future — across the whole city."

## 2. Problem & Market Need

Baku faces serious urban mobility challenges due to rapid growth and a rising number of vehicles. City residents lose significant time every day to traffic congestion, which negatively affects economic productivity and quality of life.

### 2.1 Gaps in Existing Solutions

| Product | Strength | Gap |
|---|---|---|
| Yandex Maps | Large user base, real-time data | Only shows the "current" state; no forecasting; doesn't explain local anomalies |
| Google Maps | Global coverage, reliable navigation | Doesn't account for Baku-specific factors (marshrutka/minibus routes, local events) |
| Bolt/Yango | Driver-based live data | This is a passenger transport service, not traffic management |

### 2.2 Market Opportunity

- Baku's population is 2.3+ million, and the number of vehicles is growing year over year — demand for traffic management solutions is rising.
- "Smart city" initiatives are a priority topic for municipal and government bodies — creating potential partnership and funding opportunities.
- Beyond the B2C app, traffic forecasting and anomaly detection can also be sold as an API service for B2B/B2G (taxi companies, logistics, municipality).

## 3. Vision: Baku Urban Mobility Ecosystem

This project is not an isolated app — it is the first step toward a full Urban Mobility Ecosystem for Baku.

| Component | Description | Presence in this project |
|---|---|---|
| Smart Traffic Management | Dynamic signaling, route optimization, anomaly detection | Main focus |
| Integrated Public Transport | Multimodal planner, real-time data, smart ticketing | Long-term |
| Micro-Mobility | Shared bikes/scooters, pedestrian/bike lanes | Long-term |
| Data & Analytics Hub | Central data platform, open data policy | Partial (Phase 2+) |
| Citizen Engagement | Mobile app, feedback mechanisms | Phase 2 |

## 4. Business Plan

### 4.1 Market & Competitive Analysis

While the main competitors are Yandex Maps and Google Maps, their business model is centered on advertising and broad geographic coverage — they do not invest in deep local behavior modeling. Our differentiating advantage is "depth" — a precise forecast built on Baku-specific data (marshrutka lines, local events, historical patterns).

| Criterion | Yandex/Google | Baku Smart Mobility System |
|---|---|---|
| Focus | Global, general | Local, deep (all Baku arterials) |
| Forecasting | Limited/none | Full forecast based on historical model |
| Anomaly explanation | None | Yes (z-score based detection) |
| Local transport (marshrutka) | None | Planned (Phase 2) |
| B2G/B2B API capability | Limited | Core part of the strategy |

### 4.2 Revenue Model (Monetization)

- **B2C Freemium:** Basic map/forecast free; premium features (detailed route analytics, unlimited notifications) subscription-based.
- **B2B API:** Sale of traffic forecast and anomaly data via API to taxi/logistics companies.
- **B2G Partnership:** Collaboration with Baku municipality/transport authorities within a "smart city" project — licensing or grant funding.
- **Anonymized Data Analytics:** Sale of aggregated mobility reports for city planning (with full GDPR/privacy compliance).

### 4.3 Target Customer Segments

| Segment | Need | Value proposition |
|---|---|---|
| Daily drivers (B2C) | Time savings, avoiding delays | Accurate forecasts + anomaly alerts |
| Taxi/logistics companies (B2B) | Route optimization, cost reduction | API-based forecast integration |
| Municipality/Transport Authority (B2G) | City planning, congestion reduction | Aggregated data + reports |

### 4.4 Cost Forecast (First 2 Months)

| Cost category | Month 1 | Month 2 (expansion) |
|---|---|---|
| Team (salary/compensation) | 2 people (Backend + AI) | +Mobile developer, +QA/DevOps |
| Cloud infrastructure (server, DB) | Minimal (dev/test environment) | Production-level (AWS/GCP/Azure) |
| Map API (Google Maps/Mapbox) | Free tier | Free tier + monitoring |
| AI-assisted development tools | Claude Code/Copilot license | Ongoing |
| Domain, SSL, basic tools | Minimal | Minimal |

Note: In Month 1, the main cost is only team time and development tools — cloud infrastructure is kept minimal, since the focus is building a working prototype engine, not a full production deployment.

### 4.5 Go-to-Market Strategy

1. **Phase 1 (Months 1–2):** Internal prototype + proof of concept — to present to investors/partners.
2. **Phase 2 (Months 3–4):** Limited pilot region (e.g. a few main arterials) with real user testing.
3. **Phase 3 (Months 5–6):** Negotiations with municipal/government bodies, start of B2B partnerships.
4. **Phase 4 (6+ months):** Full city-scale launch, public release of the mobile app.

## 5. Product Core & Features

The core value proposition: to optimize the travel experience across all of Baku by giving users forward-looking forecasts and anomaly alerts, beyond just the instantaneous traffic state.

### 5.1 Must-have

- A traffic map covering Baku's main arterial network (20–30 segments).
- Hourly traffic pattern forecasting (based on historical/synthetic data).
- AI-assisted web dashboard (live map view).
- "Known hotspots" — dedicated tracking of Baku's 10–15 most congested points.

### 5.2 Should-have

- Anomaly detection (z-score based).
- Route recommendation (fastest route).
- Reliability score — next to each forecast, an indicator of "how many data points it's based on."

### 5.3 Nice-to-have (Phase 2+)

- Mobile app (Android/iOS) — for real GPS data collection.
- Dedicated marshrutka (minibus) data.
- User reporting ("I see an accident" / "Road is closed").
- Weather and event/incident integration.

## 6. Team Structure & Roles

### 6.1 Phase 1 — First Month (2 people)

In the first month, no separate frontend/dashboard developer is required — both people build it together via AI-assisted development (Claude Code, etc.).

| Role | Core responsibility | AI-assisted additional work |
|---|---|---|
| Backend Developer (Java/Spring Boot) | REST APIs, DB schema, WebSocket, Docker | Web dashboard (Leaflet.js/React) — live map, forecast display |
| AI/Data Engineer | Synthetic data, prediction model, anomaly detection, FastAPI microservice | Analytics panel (Streamlit/Plotly Dash) — heatmap, statistics |

### 6.2 Phase 2 — Second Month (Team Expansion)

| New role | Priority | Core responsibility |
|---|---|---|
| Mobile Developer | Highest | Android/iOS app, real GPS data collection, map integration |
| QA/DevOps Engineer | High | CI/CD, test automation, quality review of AI-assisted code |
| UI/UX Designer | Medium | Turning the AI-assisted dashboard into a professional design |

### 6.3 Phase 3+ — Full Team (3–6 months, during scaling)

| Role | Count | Core responsibility |
|---|---|---|
| Backend Developer (Java/Spring Boot) | 2 | Scaling APIs, microservice architecture |
| AI/Data Scientist | 1–2 | Model improvement, training with real data |
| Mobile Developer | 1–2 | iOS/Android separately or cross-platform |
| Frontend Developer | 1 | Full UI/UX, dashboard expansion |
| DevOps Engineer | 1 | Production infrastructure, monitoring, scaling |
| Product Manager | 1 | Roadmap, partnership negotiations, business strategy |
| QA Engineer | 1 | Test automation, quality assurance |

## 7. Technical Architecture

### 7.1 System Architecture Overview

The system is based on a microservice principle: the Java Spring Boot main backend (API, business logic, data management) works together with a separate Python FastAPI AI/data science microservice, which is called by Spring Boot via REST.

> **Flow**
> Mobile/Web Client → Spring Boot API Gateway → [PostgreSQL (core data)] + [FastAPI AI Microservice (prediction/anomaly)] → real-time response returned to the client via WebSocket.

### 7.2 Backend Architecture (Spring Boot — Layered)

| Layer | Content |
|---|---|
| Controller Layer | REST endpoints (@RestController) — request handling and validation |
| Service Layer | Business logic, communication with the FastAPI microservice (RestTemplate/WebClient) |
| Repository Layer | Spring Data JPA — connection to PostgreSQL |
| Security Layer | Spring Security + JWT (from Phase 2) |
| WebSocket Layer | Real-time map updates via STOMP protocol |
| Config/Infra Layer | application.yml, profile-based configuration (dev/prod) |

### 7.3 API Endpoints

| Endpoint | Method | Function |
|---|---|---|
| /api/v1/gps-ping | POST | Receives location data from mobile/client |
| /api/v1/traffic-map | GET | Current traffic state across all of Baku (segment + color code) |
| /api/v1/predict | GET | Forecast for a selected route/segment |
| /api/v1/anomalies | GET | List of active anomalies |
| /api/v1/report | POST | User reports (accident/closed road) — Phase 2 |
| /ws/traffic-updates | WebSocket | Real-time map update stream |

### 7.4 Database Schema (core tables)

| Table | Key fields |
|---|---|
| road_segment | id, name, coordinates (LineString), zone |
| traffic_snapshot | segment_id, timestamp, avg_speed, congestion_level |
| historical_pattern | segment_id, day_of_week, hour, avg_speed, std_dev |
| anomaly_log | segment_id, detected_at, z_score, status |
| gps_ping | device_id, lat, lng, timestamp, speed |
| user_report | user_id, segment_id, type, description, created_at (Phase 2) |

### 7.5 AI/Data Science Microservice Architecture

- FastAPI-based REST service — returns prediction/anomaly results to requests coming from Spring Boot.
- Data pipeline: raw GPS data → aggregation (segment/hour) → historical model → forecast result.
- The modeling layer is statistical (mean + deviation) in the early stage, moving to a Prophet/time-series model in a later stage.
- Results are returned to Spring Boot in JSON format and cached in PostgreSQL (for performance).

### 7.6 Frontend/Dashboard Architecture (AI-assisted, Phase 1)

- Simple SPA (React or vanilla JS + Leaflet.js) — live map, color-coded segments.
- Analytics panel: Streamlit or Plotly Dash (can be built quickly by the AI/Data Engineer, since it's close to Python).
- In Phase 2, this evolves into a full React/Vue dashboard and mobile app.

## 8. Technology Stack (Tools)

### 8.1 Backend Tools (Java/Spring Boot Ecosystem)

- **Java 21 (LTS)** — main programming language
- **Spring Boot 3.x** — main framework — REST API, dependency injection
- **Spring Data JPA + Hibernate** — ORM layer, connection to PostgreSQL
- **Spring Security + JWT** — authentication/authorization (from Phase 2)
- **Spring WebSocket (STOMP)** — real-time map updates
- **PostgreSQL + PostGIS** — main database; PostGIS for geographic queries (road segments)
- **Maven / Gradle** — project management and build tools
- **Swagger / OpenAPI (springdoc)** — API documentation
- **JUnit 5 + Mockito** — unit and integration tests
- **Flyway** — database migration management
- **Docker + Docker Compose** — containerization, local/prod environment consistency

### 8.2 AI/Data Science Tools (Python Ecosystem)

- **Python 3.12** — main data science language
- **FastAPI** — lightweight, fast REST framework for the AI microservice
- **Pandas / NumPy** — data processing and aggregation
- **Scikit-learn** — additional tools for statistical modeling and z-score calculations
- **Prophet (Meta)** — time-series forecasting — trend + seasonality (Phase 2)
- **OSMnx + NetworkX** — OpenStreetMap road graph, route optimization (Dijkstra)
- **Streamlit / Plotly Dash** — building a fast AI-assisted analytics panel
- **Jupyter Notebook** — model prototyping and data analysis stage

### 8.3 AI-Assisted Development Tools

- **Claude Code** — fast backend/frontend code writing, refactoring, test generation
- **GitHub Copilot (alternative/supplementary)** — code completion, fast boilerplate writing
- **Claude (chat)** — architecture decisions, documentation, code review

### 8.4 DevOps / Infrastructure Tools

- **GitHub / GitHub Actions** — version control and CI/CD pipeline (from Phase 2)
- **Docker Hub / GitHub Container Registry** — container image storage
- **AWS / GCP / Azure** (choice made in Phase 2) — cloud infrastructure — production deployment
- **Prometheus + Grafana** (Phase 2+) — monitoring and performance tracking
- **Postman / Insomnia** — API testing and documentation

### 8.5 Maps & External APIs

- **Leaflet.js / Mapbox GL JS** — web map visualization (free tier)
- **Google Maps SDK** (mobile, Phase 2) — map integration for the mobile app
- **OpenStreetMap** — open-source road graph data

## 9. Data Strategy

The biggest risk when aiming for full city-scale coverage in Baku is the lack of real data. This is addressed with a two-layer approach.

### 9.1 Layer 1 — City-Scale Modeled Baseline

- A synthetic hourly traffic table covering 20–30 main arterial segments of Baku.
- Patterns based on realistic city behavior (morning/midday/evening peaks).

### 9.2 Layer 2 — Real Data Collection (from Phase 2)

- Collection of real GPS pings via the mobile app (opt-in, anonymized).
- Over time, synthetic data is gradually replaced by real data.

> **Transparency principle**
> Both in internal documentation and in any external presentation, it must be clearly stated that the data used in the early stage is a modeled baseline — this is important both ethically and for credibility.

## 10. Development Roadmap

### 10.1 Phase 1 — Month 1 (2 people)

| Week | Backend Developer | AI/Data Engineer |
|---|---|---|
| 1 | Spring Boot skeleton, DB schema (PostgreSQL), /gps-ping | Synthetic data structure, preparation of the segment list |
| 2 | /traffic-map, WebSocket infrastructure, basic web map UI (AI-assisted) | Statistical forecasting model, FastAPI skeleton |
| 3 | /predict integration (Spring→FastAPI), adding forecast display to UI | Anomaly detection (z-score), Streamlit analytics panel (AI-assisted) |
| 4 | Integration, Docker, bug-fixing, UI polishing, documentation | Data quality review, demo preparation, model documentation |

### 10.2 Phase 2 — Month 2 (Team Expansion)

- Start of mobile app development (Android/iOS or cross-platform).
- Building the real GPS data collection pipeline.
- User authentication with Spring Security + JWT.
- CI/CD pipeline (GitHub Actions) and full Docker orchestration.
- Turning the AI-assisted dashboard into a professional UI/UX.

### 10.3 Phase 3 — Months 3–6

- Real user testing in a pilot region.
- Improved forecasting model with Prophet, training on real data.
- Marshrutka tracking, user reporting, weather/event integration.
- Start of municipal/partner negotiations, launch of the B2B API.

### 10.4 Phase 4 — 6+ Months (Scaling)

- Full city-scale launch.
- Transition to other ecosystem components (public transport integration, micro-mobility).

## 11. Risks & Risk Management

| Risk | Impact | Mitigation Strategy |
|---|---|---|
| Cold-start (no real data) | High | Two-layer data strategy; transparent communication |
| Broad coverage with a 2-person team | Medium-High | Precisely limiting scope, AI-assisted development |
| Competition with Yandex/Google | Medium | Differentiation via local depth (marshrutka, historical patterns) |
| Privacy/GPS tracking sensitivity | Medium | Opt-in, anonymized data, clear privacy policy |
| Quality of AI-assisted code | Medium | Adding QA/DevOps in Phase 2, code review process |
| Funding/sustainability | Medium | Early search for revenue via B2G partnership and B2B API |

## 12. Success Metrics (KPI)

### 12.1 Technical KPIs (end of Phase 1)

- All main API endpoints operational (uptime > 95% in dev environment).
- Logical consistency of the forecast model on synthetic data (backtesting).
- Real-time WebSocket update latency < 2 seconds.

### 12.2 Product KPIs (Phase 2–3)

- Number of active users (after mobile app launch).
- Forecast accuracy (average error rate compared to real data).
- Anomaly detection precision/false-positive rate.

### 12.3 Business KPIs (Phase 3+)

- Number of B2G/B2B partnerships.
- API request volume (across B2B customers).
- Revenue growth (subscriptions + API + partnerships).

## 13. Long-Term Ecosystem Vision

After the MVP succeeds, the remaining components of the Baku Urban Mobility Ecosystem can be added in phases:

- Multimodal trip planner (metro + bus + marshrutka + taxi).
- Smart fleet management and routing.
- Dynamic signaling — real-time optimization of traffic lights.
- Open data platform — anonymized data for researchers and developers.
- On-demand public transport.
- Smart ticketing system — a single card/app for all transport types.

## 14. Conclusion

> **Closing message**
> This project, starting with a 2-person core team and AI-assisted development, builds a concrete, testable engine within 1 month that solves Baku's current traffic problem.
>
> As the team expands in subsequent phases, this engine evolves into a full mobile app, a business model, and eventually the foundation of the city's future smart mobility ecosystem.
# 🌅 NewDawn

> **Connecting communities to opportunities.**

NewDawn is an Android application that connects people who need short-term, informal work with skilled workers in their local community. Built to address township unemployment, it focuses on hyper-local, casual job opportunities.

![Android CI](https://github.com/Tsweleng10/newdawn-android/actions/workflows/build.yml/badge.svg)
![Kotlin](https://img.shields.io/badge/Kotlin-1.9-purple)
![Compose](https://img.shields.io/badge/Compose-Material3-blue)
![API](https://img.shields.io/badge/API-Railway-success)
![Database](https://img.shields.io/badge/DB-PostgreSQL-blue)

---

## 📖 Table of Contents

- [Purpose](#-purpose)
- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#-architecture)
- [Demo Video](#-demo-video)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [GitHub Workflow](#-github-workflow)
- [GitHub Actions](#-github-actions)
- [Testing](#-testing)
- [Team](#-team)
- [References](#-references)

---

## 🎯 Purpose

In South African townships, there is a mismatch between people who need short-term help (gardening, cleaning, painting, moving) and people who have the skills to do that work. Existing platforms like Cirvo, VukaWork and Groom target formal employment, leaving a gap for informal, community-level work.

**NewDawn** fills this gap by:
- Focusing on **local community opportunities** (location-based matching)
- Simplifying **informal job offers** — no CVs, no long forms
- Supporting **offline actions** with later synchronization
- Building **trust** through ratings and reviews
- Delivering **real-time notifications**

Users are **not locked** into a single role — every user can post work *and* find work.

---

## ✨ Features

| Feature | Status | Description |
|---|---|---|
| Secure Authentication | ✅ | Register/login with bcrypt-hashed passwords and JWT sessions |
| User Profiles | ✅ | Editable profile with name, email, location |
| Post a Job | ✅ | Create listings with title, category, description, location, budget |
| Browse Jobs | ✅ | Search and filter by keyword, category, location |
| Submit Offers | ✅ | Workers submit offers with price, message, availability |
| View & Accept Offers | ✅ | Posters compare offers and select a worker |
| Job Status Tracking | ✅ | Jobs move OPEN → ACTIVE → COMPLETED |
| Settings Menu | ✅ | Language, notifications, and password change |
| Notifications | 🚧 | Planned for final PoE |

---

## 🛠 Tech Stack

**Android**
- Kotlin + Jetpack Compose (Material 3)
- MVVM architecture with ViewModels + StateFlow
- Retrofit 2 + Gson (networking)
- DataStore Preferences (JWT token storage)
- Coil (image loading)

**Backend**
- Node.js 20 + Express.js
- PostgreSQL on Neon
- JWT authentication + bcrypt password hashing
- Hosted on Railway

**DevOps**
- Git + GitHub with feature-branch workflow
- GitHub Actions for automated build + unit tests

---

## 🏗 Architecture

```text
┌───────────────────────────────────────────────────────┐
│                      Android App                      │
│  ┌──────────────┐  ┌──────────────┐  ┌────────────┐   │
│  │  Compose UI  │→ │  ViewModels  │→ │ Repository │   │
│  └──────────────┘  └──────────────┘  └─────┬──────┘   │
└────────────────────────────────────────────┼──────────┘
                                             │ Retrofit
                                             │ HTTP + JWT
                                             │
┌────────────────────────────────────────────▼──────────┐
│                    Node.js REST API                   │
│  Routes → Middleware (JWT) → Handlers → pg (SQL)      │
└────────────────────────────────────────────┬──────────┘
                                             │
                                    ┌────────▼────────┐
                                    │   PostgreSQL    │
                                    │     (Neon)      │
                                    └─────────────────┘
```

The Android app **never** talks to the database directly. Every request goes through the REST API, which validates input, checks the JWT token, and runs SQL queries.

---

## 🎥 Demo Video

**▶️ [Watch the full demo on YouTube](https://youtu.be/6jjQ2usDOQw?si=nMRI0pVkNWdOkHuh)**

The video demonstrates:
1. Registration 
2. Login and session persistence
3. Posting a job
4. Browsing and searching for jobs
5. Submitting an offer as a worker

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog or later
- JDK 17
- Android device or emulator (API 24+)
- Internet connection

### Clone and Run

```bash
git clone https://github.com/Tsweleng10/newdawn-android.git
cd newdawn-android
```

1. Open the project in Android Studio
2. Wait for Gradle sync
3. Connect a physical Android device (USB debugging enabled)
4. Click Run ▶

The app automatically connects to the live API at:
`https://newdawn-api-production.up.railway.app/`

### Test Account

Register your own account in the app. Or use the demo credentials:

```
Email: demo@newdawn.com
Password: demo1234
```

---

## 📡 API Reference

Base URL: `https://newdawn-api-production.up.railway.app/`

### Authentication

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login and receive JWT |
| GET | `/api/auth/me` | Get current user |
| PUT | `/api/auth/settings` | Update language/notifications |
| PUT | `/api/auth/change-password` | Change password |

### Jobs

| Method | Endpoint | Description |
|---|---|---|
| GET | `/api/jobs` | List open jobs (`?search=`, `?category=`, `?location=`) |
| GET | `/api/jobs/my` | List my posted jobs |
| GET | `/api/jobs/{id}` | Get single job |
| POST | `/api/jobs` | Create a job |
| PUT | `/api/jobs/{id}/status` | Update job status |

### Offers

| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/offers/job/{jobId}` | Submit an offer |
| GET | `/api/offers/job/{jobId}` | View offers for my job |
| GET | `/api/offers/my` | View offers I submitted |
| PUT | `/api/offers/{id}/accept` | Accept an offer |

Authenticated endpoints require the header:

```text
Authorization: Bearer <jwt_token>
```

**Register example**

```http
POST /api/auth/register
Content-Type: application/json

{
  "full_name": "Sarah Lekoane",
  "email": "sarah@example.com",
  "password": "test1234",
  "location": "Soweto"
}
```

**Response**

```json
{
  "user": {
    "id": 1,
    "full_name": "Sarah Lekoane",
    "email": "sarah@example.com",
    "location": "Soweto"
  },
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 🌿 GitHub Workflow

We used a feature-branch + Pull Request workflow:

1. Each feature was developed on its own branch (`feature/auth`, `feature/jobs`, `feature/offers-settings`)
2. When complete, the developer opened a Pull Request
3. Another team member reviewed the changes
4. The PR was merged into `master` only after review

This is visible in the Pull Requests tab of this repository.

---

## ⚙️ GitHub Actions

Automated CI is configured in `.github/workflows/build.yml`. On every push to `master` or any `feature/*` branch, GitHub Actions:

1. Checks out the code
2. Sets up JDK 17
3. Grants execute permission to `gradlew`
4. Runs `./gradlew build` — compiles the app
5. Runs `./gradlew testDebugUnitTest` — executes unit tests

Status: `https://github.com/Tsweleng10/newdawn-android/actions/workflows/build.yml/badge.svg`

A green badge proves the code compiles and passes tests on a clean machine — not just on the developer's laptop.

---

## 🧪 Testing

### Unit Tests

Located in `app/src/test/java/com/example/newdawn/`. Run with:

```bash
./gradlew testDebugUnitTest
```

### Instrumented Tests

Located in `app/src/androidTest/java/com/example/newdawn/`. Run on a device with:

```bash
./gradlew connectedAndroidTest
```

### Manual Test Checklist

- ☑ Register with valid data → success
- ☑ Register with empty fields → validation error, no crash
- ☑ Register with mismatched passwords → validation error
- ☑ Register with existing email → API 409 displayed
- ☑ Login with correct credentials → success
- ☑ Login with wrong password → "Invalid email or password"
- ☑ Session persists after closing and reopening
- ☑ Post a job with all fields → appears in Recommended Jobs
- ☑ Post a job with empty fields → validation error
- ☑ Search jobs by keyword → filtered results
- ☑ Search with no results → friendly empty state
- ☑ Submit an offer → appears in Offers Received
- ☑ Submit duplicate offer → 409 error displayed
- ☑ Accept offer → job status changes to ACTIVE
- ☑ Change password → old password validated
- ☑ Logout → token cleared, redirected to Login

---

## 👥 Team

| Name | Student Number | Role |
|---|---|---|
| Joshua Tsweleng | st10451745 | Lead — Backend, Integration, Person 4 (Offers & Settings) |
| Fortune Lemekwana | st10450241 | Person 3 — Jobs & Home |
| Phuti Magwai | st10452585 | Person 2 — Authentication & Profile |

---

## 🤖 AI Usage Disclosure

In accordance with the OPSC6312 assessment requirements, this project used AI tools as follows:

**Tools used:** DeepSeek (code generation, debugging, architecture), ChatGPT (debugging), DALL·E (placeholder assets).

**How AI was used:**
- **Backend scaffolding** — initial Express.js folder structure and route templates were drafted with AI assistance, then manually reviewed, tested with Postman, and adapted to our PostgreSQL schema.
- **Android setup** — the Retrofit configuration, DataStore wrapper, and ViewModel patterns were informed by AI-generated examples. We corrected them to match our specific API contract (e.g. `full_name` vs `fullName`, `/api/auth/me` vs `/api/auth/profile`, server-side bcrypt vs client-side SHA-256).
- **Debugging** — during merges, AI helped diagnose issues like missing `INTERNET` permission in `AndroidManifest.xml`, unresolved references after branch merges, and package name conflicts from teammate branches.
- **Documentation** — this README was structured with AI assistance; content and technical decisions are our own.

**What we did NOT do:** We did not submit code we could not explain. We did not use AI to write the Section A/B research reports. Every AI-suggested snippet was tested by running the app against the live API on a physical device.

---

## 📚 References

- Android Developers. (2024). *Jetpack Compose Documentation*. https://developer.android.com/jetpack/compose
- Android Developers. (2024). *Guide to App Architecture*. https://developer.android.com/topic/architecture
- GitHub. (2024). *Automated Build Android App with GitHub Actions*. https://github.com/marketplace/actions/automated-build-android-app-with-github-action
- Neon. (2024). *Serverless PostgreSQL*. https://neon.tech/docs
- Node.js Foundation. (2024). *Node.js Documentation*. https://nodejs.org/docs
- OpenJS Foundation. (2024). *Express.js Documentation*. https://expressjs.com
- Railway. (2024). *Deploy Node.js Apps*. https://docs.railway.app
- Square Inc. (2024). *Retrofit*. https://square.github.io/retrofit
- Android Developers. (2024). *DataStore*. https://developer.android.com/topic/libraries/architecture/datastore

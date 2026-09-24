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
- [Screenshots](#-screenshots)
- [Demo Video](#-demo-video)
- [Getting Started](#-getting-started)
- [API Reference](#-api-reference)
- [GitHub Workflow](#-github-workflow)
- [GitHub Actions](#-github-actions)
- [Testing](#-testing)
- [Team](#-team)
- [AI Usage Disclosure](#-ai-usage-disclosure)
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

## 📸 Screenshots

| Splash | Login | Register | Home |
|---|---|---|---|
| ![Splash](docs/splash.png) | ![Login](docs/login.png) | ![Register](docs/register.png) | ![Home](docs/home.png) |

| Find Work | Job Details | Submit Offer | View Offers |
|---|---|---|---|
| ![Find](docs/findwork.png) | ![Details](docs/jobdetails.png) | ![Offer](docs/submitoffer.png) | ![Offers](docs/viewoffers.png) |

| My Jobs | Profile | Settings | Notifications |
|---|---|---|---|
| ![MyJobs](docs/myjobs.png) | ![Profile](docs/profile.png) | ![Settings](docs/settings.png) | ![Notif](docs/notifications.png) |

*(Add screenshots to a `docs/` folder in the repo. Rename or remove rows you haven't captured yet.)*

---

## 🎥 Demo Video

**▶️ [Watch the full demo on YouTube](https://youtu.be/REPLACE_WITH_YOUR_VIDEO_ID)**

The video demonstrates:
1. Registration with password encryption proof (bcrypt hash in the DB)
2. Login and session persistence
3. Posting a job
4. Browsing and searching for jobs
5. Submitting an offer as a worker
6. Viewing and accepting offers as a poster
7. Settings menu (language, notifications, password change)
8. GitHub Actions passing on a clean machine

---

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

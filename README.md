# Fitness Monolith 🏋️‍♂️

A robust, monolithic Spring Boot backend application designed for fitness management. This project features secure JWT-based authentication and integrates Google's Gemini AI to provide personalized fitness recommendations.

## 🚀 Features

* **Secure Authentication:** Implements JSON Web Tokens (JWT) for secure login, session management, and role-based access control.
* **AI-Powered Recommendations:** Integrates `Google Gemini 2.5 Flash` API via Spring WebFlux (`WebClient`) to generate dynamic, AI-driven fitness advice and workout recommendations based on user prompts.
* **Environment Configuration:** Securely manages sensitive credentials like API keys and JWT secrets using `dotenv-java`.
* **Monolithic Architecture:** A centralized and easy-to-manage Spring Boot architecture.

## 🛠️ Tech Stack

* **Language:** Java
* **Framework:** Spring Boot
* **Security:** Spring Security, JSON Web Token (jjwt)
* **HTTP Client:** Spring WebFlux (WebClient)
* **AI Integration:** Google Generative AI (Gemini API)
* **Build Tool:** Maven
* **Environment Management:** `dotenv-java`

## ⚙️ Prerequisites

Before you begin, ensure you have the following installed:
* [Java Development Kit (JDK) 17](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or higher
* [Apache Maven](https://maven.apache.org/download.cgi)
* A valid Google Gemini API Key

## 🛠️ Setup & Installation

**1. Clone the repository:**
```bash
git clone [https://github.com/kshitij3103/fitness_monolith.git](https://github.com/kshitij3103/fitness_monolith.git)
cd fitness_monolith

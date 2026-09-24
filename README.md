# 💸 ExpenseFlow

> **Track your expenses before your expenses track you.** 🗿

A **Spring Boot Microservices** project for tracking expenses, managing categories, and generating reports — because apparently checking your bank balance isn't enough. 😭

## 🧠 What it does

* 🔐 **Auth Service** — JWT said *no unauthorized access*
* 💸 **Expense Service** — tracks where the money disappeared
* 🏷️ **Category Service** — Food? Travel? *Financial mistakes?*
* 📊 **Report Service** — turns spending into numbers you don't wanna see
* 🌐 **API Gateway** — traffic controller fr
* 🔎 **Eureka Server** — "Where is bro's service?" → *found it*
* ⚖️ **Load Balancing** — sharing the suffering
* 🧪 **Testing** — trust issues, but make it automated

## 🏗️ Architecture

```text
                 👤 Client
                    │
                    ▼
             🌐 API Gateway
                    │
       ┌────────────┼────────────┐
       ▼            ▼            ▼
   🔐 Auth      💸 Expense    🏷️ Category
   Service       Service       Service
                    │
                    ▼
               📊 Report
                Service

             🔎 Eureka Server
          "I know where everyone is."
```

## 🛠️ Tech Stack

`Java` • `Spring Boot` • `Spring Cloud` • `JWT` • `Spring Security` • `Eureka` • `PostgresSQL` • `Maven`

## 🚀 Run It

```bash
mvn clean install
mvn spring-boot:run
```

Then pray everything registers with Eureka. 🙏💀

---

⭐ Star the repo if your money also mysteriously disappears.

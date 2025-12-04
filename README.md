# Movies Mediator – Intégration Multi-Sources de Données Cinéma

Médiateur de données permettant de centraliser et interroger plusieurs sources d’information sur les films : base locale, DBpedia et Open Movie Database.

---

## 📌 Sommaire

- [Movies Mediator – Intégration Multi-Sources de Données Cinéma](#-movies-mediator--intégration-multi-sources-de-données-cinéma)
  - [📌 Sommaire](#-sommaire)
  - [🎯 Objectif du projet](#-objectif-du-projet)
  - [✨ Fonctionnalités principales](#-fonctionnalités-principales)
  - [🧩 Structure du projet / Architecture](#-structure-du-projet--architecture)
  - [🚀 Installation \& Déploiement](#-installation--déploiement)
    - [Prérequis](#prérequis)
    - [Étapes d’installation](#étapes-dinstallation)
  - [🛠️ Technologies \& Outils utilisés](#️-technologies--outils-utilisés)
  - [👥 Auteurs \& Licence](#-auteurs--licence)

---

## 🎯 Objectif du projet

Le projet vise à **centraliser et interroger plusieurs sources de données cinéma** pour fournir des informations complètes sur les films :

1. **Base de données locale** : informations sur les films (titre, date de sortie, genre, distributeur, budget, revenus aux États-Unis et mondiaux), alimentée via ETL Talend et extraction de pages web HTML.  
2. **DBpedia (LOD)** : informations sur le réalisateur, les acteurs et les producteurs d’un film.  
3. **Open Movie Database (OMDb)** : résumé et informations complémentaires sur les films via service REST.  

Le médiateur centralise les informations à travers **un seul modèle relationnel** et relie les données par le titre du film.

---

## ✨ Fonctionnalités principales

- Requête par **titre de film** : affiche toutes les informations disponibles (date, genre, distributeur, budget, revenus, réalisateur, acteurs, producteur, résumé).  
- Requête par **nom d’acteur** : liste tous les films où il a joué avec leurs informations principales.  
- Intégration multi-source :
  - Base locale via JDBC  
  - DBpedia via SPARQL avec Jena  
  - OMDb via service REST et parsing XML/XPath  
- Interface textuelle simple en Java pour interroger les films et acteurs.  
- Gestion automatique des données extraites depuis HTML via **JSoup** pour compléter la base locale.  
- Utilisation de **Talend** pour alimenter la base locale.

---

## 🧩 Structure du projet / Architecture

```text
/ (racine)
├── src/
  ├── main/
    ├── java/
      ├── databaseClient/ 
        ├── DatabaseClient.java # Gestion de la base de données (requêtes)
        ├── JdbcConnection.java # Connexion à la base de données
        └── MovieScrapper.java  # Extraction HTML par genre
      ├── entity/               # Gestion des acteurs et des films
      ├── HTTPClient/           # Appels REST et parsing XML
      ├── mediator/             # Classe logique du médiateur
      ├── sparQL/               # Requêtes SPARQL avec Jena
      └── ConsoleInterface.java # Main
├── movieBudgets.csv            # Données de films de base
├── creation.sql                # Script SQL pour la création de la base de données 
└── pom.xml                     # Dépendances et build du projet
```

---

## 🚀 Installation & Déploiement

### Prérequis

- Java 11+  
- Base locale SQL (MySQL, PostgreSQL, etc.)  
- Clé API OMDb  
- Librairie **Jena** pour SPARQL  
- JSoup pour extraction HTML
- Talend  

### Étapes d’installation

1. Cloner le dépôt :

    ```bash
    git clone https://github.com/nchrismant/movies_mediator.git
    cd movies_mediator
    ```

2. Importer la base de données MySQL à partir du fichier `creation.sql`.
3. Créer les fichiers des films par genre en exécutant la classe `src/main/java/databaseClient/MovieScrapper.java`.
4. Utiliser Talend pour alimenter la base de données locale en utilisant les fichiers crées à l'étape 2. et le fichier `movieBudgets.csv` (Left Outer Join).
5. Lancer le médiateur

    ```java
    java src/main/java/ConsoleInterface.java
    ```

---

## 🛠️ Technologies & Outils utilisés

| Technologie         | Rôle              |
| ------------------- | ----------------- |
| **Java**             | Langage principal côté serveur |
| **MySQL**           | Base de données |
| **JDBC**      | Librairie pour connexion base de données |
| **Jena**      | Librairie pour SPARQL |
| **JSoup** | Librairie pour extraction HTML |
| **Talend** | ETL pour alimentation base de données |
| **REST / HTTP GET** | Requêtes |
| **XML / Xpath** | Parsing du résumé |

---

## 👥 Auteurs & Licence

- **CHRISMANT Nathan** — Étudiant Master IISC - SIC 1ère année, Cergy Paris Université.
- **KUCHLER Ulysse** — Étudiant Master IISC - SIC 1ère année, Cergy Paris Université.

Ce projet est distribué sous licence Open Source Libre.

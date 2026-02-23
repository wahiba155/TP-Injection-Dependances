# TP - Injection des Dépendances (DI Framework)

**Auteur :** Wahiba Moussaoui  
**Module :** Java EE  

---

## Partie 1 – Injection des Dépendances avec Spring

### 1. Création des interfaces et implémentations

- Création de l'interface **IDao** avec une méthode `getData()`
- Création de **DaoImpl**, implémentation de IDao
- Création de l'interface **IMetier** avec une méthode `calcul()`
- Création de **MetierImpl**, implémentation de IMetier en utilisant le **couplage faible** (dépendance via l'interface IDao)

### 2. Injection des dépendances

#### a. Par instanciation statique
Instanciation manuelle des objets `DaoImpl` et `MetierImpl`, puis injection du dao dans le métier via le setter.

#### b. Par instanciation dynamique (Réflexion Java)
Utilisation de `Class.forName()` et de l'API Reflection pour instancier les classes dynamiquement à partir d'un fichier de configuration texte, puis injection par invocation de la méthode setter.

#### c. En utilisant Spring – Version XML
Déclaration des beans `dao` et `metier` dans un fichier `ApplicationContext.xml`, avec injection de la dépendance via la balise `<property>`.

#### d. En utilisant Spring – Version Annotations
Utilisation des annotations `@Component` / `@Repository` / `@Service` et `@Autowired`, avec une classe de configuration annotée `@Configuration` et `@ComponentScan`.

---

## Partie 2 – Mini Framework d'Injection des Dépendances

### Description

Développement d'un mini framework similaire à Spring IOC permettant à un programmeur de gérer l'injection des dépendances entre les composants de son application.

### Fonctionnalités

**Version XML**
- Lecture d'un fichier `beans.xml` de configuration
- Mapping XML vers objets Java grâce à **JAXB** (Jakarta XML Binding / OXM)
- Instanciation automatique des beans et injection des dépendances déclarées

**Version Annotations**
- Utilisation d'une annotation personnalisée `@Inject`
- Détection automatique des dépendances par scan des champs annotés
- Injection sans configuration XML

### Types d'injection supportés

- **Par constructeur** : injection via le constructeur de la classe cible
- **Par setter** : injection via les méthodes setXxx()
- **Par attribut (Field)** : accès direct au champ via la réflexion Java (`field.setAccessible(true)`)

### Technologies utilisées

| Technologie | Usage |
|---|---|
| Java 17 | Langage principal |
| Maven | Gestion des dépendances |
| JAXB | Mapping XML ↔ Objets Java |
| Spring Framework | Version Spring (Partie 1) |
| Réflexion Java | Instanciation et injection dynamiques |

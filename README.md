# TP - Injection des Dépendances (DI Framework)

**Auteur :** Wahiba Moussaoui  
**Module :** Java EE  

---

## Structure du projet

```
src/
├── main/
│   ├── java/
│   │   ├── ma.enset.dao/
│   │   │   ├── IDao.java
│   │   │   └── DaoImpl.java
│   │   ├── ma.enset.metier/
│   │   │   ├── IMetier.java
│   │   │   └── MetierImpl.java
│   │   ├── ma.enset.ioc/
│   │   │   ├── MiniApplicationContext.java
│   │   │   ├── Bean.java
│   │   │   ├── Beans.java
│   │   │   ├── Property.java
│   │   │   └── Inject.java
│   │   ├── ma.enset.config/
│   │   │   └── Config.java
│   │   └── ma.enset.presentation/
│   │       ├── Presentation.java
│   │       ├── Presentation2.java
│   │       ├── PresentationAnnotation.java
│   │       └── TestMiniFramework.java
│   └── resources/
│       ├── beans.xml
│       └── ApplicationContext.xml
```

---

## Partie 1 – Injection des Dépendances avec Spring

### 1. Création des interfaces et implémentations

#### IDao.java
```java
package ma.enset.dao;

public interface IDao {
    double getData();
}
```

#### DaoImpl.java
```java
package ma.enset.dao;

import org.springframework.stereotype.Repository;

@Repository
public class DaoImpl implements IDao {
    @Override
    public double getData() {
        System.out.println("Version Base de Données");
        return 23.5;
    }
}
```

#### IMetier.java
```java
package ma.enset.metier;

public interface IMetier {
    double calcul();
}
```

#### MetierImpl.java (couplage faible)
```java
package ma.enset.metier;

import ma.enset.dao.IDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MetierImpl implements IMetier {
    @Autowired
    private IDao dao;

    @Override
    public double calcul() {
        double data = dao.getData();
        return data * 2;
    }

    public void setDao(IDao dao) {
        this.dao = dao;
    }
}
```

---

### 2. Injection des dépendances

#### a. Par instanciation statique

```java
// Presentation.java
public class Presentation {
    public static void main(String[] args) {
        DaoImpl dao = new DaoImpl();
        MetierImpl metier = new MetierImpl();
        metier.setDao(dao);
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

#### b. Par instanciation dynamique (Réflexion)

```java
// Presentation2.java
public class Presentation2 {
    public static void main(String[] args) throws Exception {
        // Lecture du fichier de config
        Scanner scanner = new Scanner(new File("config.txt"));
        String daoClassName = scanner.nextLine();
        String metierClassName = scanner.nextLine();

        Class<?> cDao = Class.forName(daoClassName);
        IDao dao = (IDao) cDao.getDeclaredConstructor().newInstance();

        Class<?> cMetier = Class.forName(metierClassName);
        IMetier metier = (IMetier) cMetier.getDeclaredConstructor().newInstance();

        Method setDao = cMetier.getMethod("setDao", IDao.class);
        setDao.invoke(metier, dao);

        System.out.println("Résultat : " + metier.calcul());
    }
}
```

#### c. Version Spring – XML

**ApplicationContext.xml**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="dao" class="ma.enset.dao.DaoImpl"/>

    <bean id="metier" class="ma.enset.metier.MetierImpl">
        <property name="dao" ref="dao"/>
    </bean>

</beans>
```

```java
// PresentationXML.java
public class PresentationXML {
    public static void main(String[] args) {
        ApplicationContext context =
            new ClassPathXmlApplicationContext("ApplicationContext.xml");
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

#### d. Version Spring – Annotations

**Config.java**
```java
package ma.enset.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("ma.enset")
public class Config {}
```

```java
// PresentationAnnotation.java
public class PresentationAnnotation {
    public static void main(String[] args) {
        ApplicationContext context =
            new AnnotationConfigApplicationContext(Config.class);
        IMetier metier = context.getBean(IMetier.class);
        System.out.println("Résultat : " + metier.calcul());
    }
}
```

---

## Partie 2 – Mini Framework d'Injection des Dépendances

### Description

Développement d'un mini framework d'injection des dépendances similaire à Spring IOC, supportant :
- Configuration via fichier **XML** (avec JAXB / OXM)
- Configuration via **annotations**
- Injection par **constructeur**, **setter**, et **champ (field)**

---

### Annotation `@Inject`

```java
package ma.enset.ioc;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface Inject {}
```

---

### Classes JAXB pour le mapping XML

**Bean.java**
```java
@XmlRootElement
public class Bean {
    private String id;
    private String className;
    private List<Property> properties;
    // getters / setters
}
```

**Property.java**
```java
public class Property {
    private String name;
    private String ref;
    private String value;
    // getters / setters
}
```

**Beans.java**
```java
@XmlRootElement(name = "beans")
public class Beans {
    private List<Bean> beans;
    // getters / setters
}
```

---

### beans.xml (Configuration)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans>
    <bean id="dao" class="ma.enset.dao.DaoImpl"/>
    <bean id="metier" class="ma.enset.metier.MetierImpl">
        <property name="dao" ref="dao"/>
    </bean>
</beans>
```

---

### MiniApplicationContext.java

```java
package ma.enset.ioc;

public class MiniApplicationContext {
    private Map<String, Object> beansMap = new HashMap<>();

    // Version XML
    public MiniApplicationContext(String xmlFile) throws Exception {
        JAXBContext context = JAXBContext.newInstance(Beans.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Beans beans = (Beans) unmarshaller.unmarshal(new File(xmlFile));

        // Instanciation des beans
        for (Bean bean : beans.getBeans()) {
            Class<?> cls = Class.forName(bean.getClassName());
            Object instance = cls.getDeclaredConstructor().newInstance();
            beansMap.put(bean.getId(), instance);
        }

        // Injection des propriétés
        for (Bean bean : beans.getBeans()) {
            Object instance = beansMap.get(bean.getId());
            if (bean.getProperties() != null) {
                for (Property property : bean.getProperties()) {
                    // Injection par setter
                    Object dep = beansMap.get(property.getRef());
                    String setter = "set" + Character.toUpperCase(property.getName().charAt(0))
                                  + property.getName().substring(1);
                    Method m = instance.getClass().getMethod(setter, dep.getClass().getInterfaces()[0]);
                    m.invoke(instance, dep);

                    // Injection par field
                    // Field f = instance.getClass().getDeclaredField(property.getName());
                    // f.setAccessible(true);
                    // f.set(instance, dep);
                }
            }
        }
    }

    // Version Annotations
    public MiniApplicationContext(Class<?>... classes) throws Exception {
        for (Class<?> cls : classes) {
            Object instance = cls.getDeclaredConstructor().newInstance();
            beansMap.put(cls.getSimpleName(), instance);
        }
        // Injection via @Inject sur les fields
        for (Object instance : beansMap.values()) {
            for (Field field : instance.getClass().getDeclaredFields()) {
                if (field.isAnnotationPresent(Inject.class)) {
                    field.setAccessible(true);
                    for (Object dep : beansMap.values()) {
                        if (field.getType().isAssignableFrom(dep.getClass())) {
                            field.set(instance, dep);
                        }
                    }
                }
            }
        }
    }

    public Object getBean(String id) {
        return beansMap.get(id);
    }
}
```

---

### Test du Mini Framework

**TestMiniFramework.java**
```java
public class TestMiniFramework {
    public static void main(String[] args) throws Exception {
        // Version XML
        MiniApplicationContext ctxXml = new MiniApplicationContext("beans.xml");
        IMetier metier = (IMetier) ctxXml.getBean("metier");
        System.out.println("XML   => " + metier.calcul());

        // Version Annotations
        MiniApplicationContext ctxAnnot =
            new MiniApplicationContext(DaoImpl.class, MetierImpl.class);
        IMetier metier2 = (IMetier) ctxAnnot.getBean("MetierImpl");
        System.out.println("Annot => " + metier2.calcul());
    }
}
```

---

## Technologies utilisées

| Technologie | Usage |
|---|---|
| Java 17 | Langage principal |
| Maven | Gestion des dépendances |
| JAXB | Mapping XML ↔ Objets Java |
| Spring Framework | Version Spring du projet |
| Réflexion Java | Instanciation dynamique |

---

## Résultats attendus

```
Version Base de Données
XML   => 47.0

Version Base de Données
Annot => 47.0
```

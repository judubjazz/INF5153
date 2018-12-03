# Travail pratique 3

## Description

Conception d'un petit programme dont l'exécutable est nommé `tp3.jar` permettant de jouer au jeu de bataille navale.

#### Contexte
* Univeristé: UQÀM
* Cours : Génie logiciel
* Sigle : INF5153
* Enseignant : Jacques Berger

## Auteur

- Julien Guité-Vinet  (GUIJ09058407)
- Amine Amari (AMAA16049302)

## Fonctionnement

### Jouer contre l'ordinateur

### Jouer en ligne

### Sauver une partie

### Charger une partie


## exécution du logiciel
- pour executer le programme 
    * Java de version >= 1.8.0 doit être installé 
    * Les ports 8090 et 9291 ne doivent pas être activement en service
        * si les ports sont en service, il est possible de fermer ceux-ci avec la commande unix
        ``` sh
        fuser -k 8090/tcp
        fuser -k 9291/tcp
        ```
- Pour exécuter le programme, ouvrir le terminal à la racine du projet
- La commande `java -jar tp3.jar` lance le serveur tomcat sur le port 8090
- Ensuite, l'application est disponible en navigant au url [http://localhost:8090/](http://localhost:8090/)


## Plateformes supportées

- testé sur linux mint 18.2 sonya avec chrome version 63 

## Dépendances

- [spring-boot-starter](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter) pour initialiser spring.
- [spring-boot-starter-web](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-web), pour joindre tomcat et spring.
- [spring-boot-starter-thymeleaf](https://mvnrepository.com/artifact/org.springframework.boot/spring-boot-starter-thymeleaf), pour générer les templates html.
- [thymeleaf-layout-dialect](https://mvnrepository.com/artifact/nz.net.ultraq.thymeleaf/thymeleaf-layout-dialect) pour créer des layouts html avec thymeleaf.
- [bootstrap](https://mvnrepository.com/artifact/org.webjars/bootstrap) pour des classes de base de css.
- [json-lib](https://mvnrepository.com/artifact/net.sf.json-lib/json-lib) pour traiter le json en java.
- [socket.io-client](https://mvnrepository.com/artifact/io.socket/socket.io-client)(optionel) pour créer un client écoutant des sockets en java.
- [netty-socketio](https://mvnrepository.com/artifact/com.corundumstudio.socketio/netty-socketio) pour créer le serveur ouvrant les sockets en java.
- [mongo-java-driver](https://mvnrepository.com/artifact/org.mongodb) pour avoir un client mongo en java.


## Références

## Dépôt Github
[https://github.com/judubjazz/INF5153](https://github.com/judubjazz/INF5153)
## Division des tâches

Les tâches accomplies
- [X] Initialisation du dépôt ```Julien```
  - [X] README ```Julien```
- [X] Gestion des dépendances
  - [X] Gradle ```Julien```
  - [X] Maven ```Amine```  ```Julien```  
- [X] Configuration de Spring ```Julien```
- [ ] Programation du Front-end 
  - [X] Programmation du menu ```Amine```
  - [X] Programmation des grilles
    - [X] javascript ```Amine```
    - [X] css ```Amine```
    - [X] html ```Julien```
  - [X] Programation des requêtes envoyées au serveur ```Julien```
- [ ] Programation du Back-End
  - [X] Configuration de Spring ```Julien```
  - [X] Programation des interfaces ```Julien```
  - [X] Programation des entités
    - [X] Classes ```Julien```
    - [X] Héritage ```Julien```
  - [X] Programmation du Ai ```Julien```
  - [ ] Application des GOF
    - [X] Memento ```Julien```
    - [X] Singleton ```Julien```
    - [X] Factory Method ```Julien```
    - [X] Builder ```Julien```
    - [X] Abstract Factory ```Julien```
    - [ ] State
  - [X] Programation des routes ```Julien```   ```Amine```
  - [X] Programation des sockets ```Julien```
  - [ ] Validation des requêtes
  - [ ] Gestion des erreurs
- [ ] Programation de la base de données
  - [ ] Programation de l'interface
  - [X] XML
    - [X] Save ```Amari``` ```Julien```
    - [X] Load ```Amari``` ```Julien```
    - [X] Delete ```Amine```
  - [ ] Mongo
    - [ ] Save
    - [ ] Load
    - [ ] Delete
- [X] Test de déploiement ```Julien```    
    
## État du projet
preuve de concept du polymorphisme.

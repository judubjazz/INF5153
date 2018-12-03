# Travail pratique 3

## Description

Conception d'un petit programme dont l'exécutable est nommé battleship et
qui permet de jouer au jeu de bataille navale

#### Contexte
* Univeristé: UQÀM
* Cours : Génie logiciel
* Sigle : INF5153
* Enseignant : Jacques Berger

## Auteur

- Julien Guité-Vinet  (GUIJ09058407)
- Amari

## Fonctionnement

Pour avoir plus d'informations sur le fonctionnement général du programme, il
suffit d'entrer la commande
```
bin/tp2 --help
```


### Récupération des données

Les données géographiques sont disponibles dans le fichier JSON
[countries.json](/countries.json).

On trouve également des images des drapeaux au format PNG dans le dossier [data](/data).

### Jouer contre l'ordinateur



### Jouer en ligne



## Compilation

- Pour compiler le programme, ouvrir le terminal à la racine du projet
- La commande `make` crée l'exécutable `tp3` dans le dossier [bin](/bin)
    - Pour executer, entrer la commande suivante dans le terminal :
    
```sh
make 
```

- La commande `make clean` supprime les fichiers inutiles (`.o`, `.html`,
  `.swp`, etc.).
    * Pour executer, entrer la commande suivante dans le terminal :
    
```sh
make clean 
```

## Plateformes supportées

- testé sur linux mint 18.2 sonya
- testé sur chrome version 63 

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
  - [X] Gradle ```Julien```
  - [X] Maven ```Amari```  ```Julien```  
  - [X] README (Julien)
- [X] Configuration de Spring ```Julien```
- [ ] Programation du Front-end 
  - [X] Programmation du menu ```Amari```
  - [X] Programmation des grilles
    - [X] javascript ```Amari```
    - [X] css ```Amari```
    - [X] html ```Julien```
  - [X] Programation des requêtes envoyées au serveur ```Julien```
- [ ] Programation du Back-End
  - [X] Configuration de Spring ```Julien```
  - [X] Programation des interfaces ```Julien```
  - [X] Programmation du Ai ```Julien```
  - [ ] Application des GOF
    - [X] Memento ```Julien```
    - [X] Singleton ```Julien```
    - [X] Factory Method ```Julien```
    - [X] Builder ```Julien```
    - [X] Abstract Factory ```Julien```
    - [ ] State
  - [X] Programation des routes ```Julien```   ```Amari```
  - [X] Programtion des sockets ```Julien```
  - [ ] Validation des requêtes
  - [ ] Gestion des erreurs
- [ ] Programation de la base de données
  - [ ] Programation de l'interface
  - [X] XML
    - [X] Save ```Amari``` ```Julien```
    - [X] Load ```Amari``` ```Julien```
    - [X] Delete ```Amari```
  - [ ] Mongo
    - [ ] Save
    - [ ] Load
    - [ ] Delete
- [ ] Test de déploiement    
    
## État du projet
preuve de concept du polymorphisme.

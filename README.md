GodStemIt
=========

Repository for storing applications helpful for developing sentiment classification system.
A SQL [script] (database.sql) with initial data can be used for research purposes.

## Analyze
R scripts responsible for training a model from data gathered in MySQL database.

#### Keywords
R, MySQL, caret package, Naive Bayes, Generalized Boosted Models, Random Forest

## Stemmer
Java Application performing stemming and pre-processing on sentences stored in MySQL database.

#### Keywords
Java 1.8, MySQL, Maven, Morphologic Dictionary

#### Usage
    cd Stemmer
    mvn clean package
    java -jar target/stemmer-1.0.jar
    
## IsItGood
Java Web Application + R server working together to provide a REST API (protected with OAuth) for predicting sentiments.

#### Keywords
Java 1.8, Spring Boot, OAuth 2.0, Gradle, R, RServe

#### Usage
    cd IsItGood
    gradle clean build bootRun
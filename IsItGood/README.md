# IsItGood
Application exposing a REST API for making predictions

## Prerequisites
- Java (1.8)
- Gradle (2.2.1)
- R (3.1.1)
- package RServe (1.7.3)
- package tm (0.6)
- package caret (6.0.37)

## Start/Stop RServe
Running Rserve on port 6311 (extra configuration specified in `Rserv.conf`)

    R CMD Rserve --RS-conf Rserv.conf
    killall Rserve
    
    
## Start application
    gradle build bootRun
    
## Send request
### Secured
According to [this link](https://github.com/royclarkson/spring-rest-service-oauth).

    curl -XPOST  -H "Accept: application/json" -vu clientapp:123456 http://localhost:8080/oauth/token -d "password=spring&username=roy&grant_type=password&scope=read%20write&client_secret=123456&client_id=clientapp"
    curl -XGET -H "Authorization: Bearer 960e9f00-0976-4f38-8681-a00e0c167de8" http://localhost:8080/greeting


### Not secured

    curl -XPOST -H "Content-type: application/json" localhost:8080/predict -d '{ "original": "W tym sklepie niezmiernie podobała mi się obsługa. Zawsze przyjemnie." }'

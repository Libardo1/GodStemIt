# IsItGood
Application exposing a REST API for making predictions

## Prerequisites
- Java (1.8)
- Gradle (2.2.1)
- R (3.1.1)
- package RServe (1.7.3)
- package tm (0.6)
- package caret (6.0.37)

### Start/Stop RServe
Running Rserve on port 6311 (extra configuration specified in `Rserv.conf`)

    R CMD Rserve --RS-conf Rserv.conf
    killall Rserve
    
    
### Start application
    gradle build bootRun

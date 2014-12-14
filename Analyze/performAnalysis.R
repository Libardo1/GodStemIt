library(plyr)
library(dplyr)
library(tm)
library(caret)
library(foreach)
library(doParallel)
library(pracma)

setwd("/Users/khozzy/GodStemIt/Analyze/")
source("getRecords.R")
source("naiveBayes.R")
source("randomForest.R")
source("gbm.R")

set.seed(424232)
cl <- makeCluster(4, type = "PSOCK", oufile = " ")
registerDoParallel(cl)

df <- prepareDataset()

message("Creating train, test datasets ...")
inTrain <- createDataPartition(df$CLASS, p = 0.8, list = FALSE)

training <- df[inTrain,]
testing <- df[-inTrain,]

message("Training Naive Bayes model ...")
nb <- trainNaiveBayes(training)

message("Training Random Forest model ...")
rf <- trainRandomForest(training)

message("Training GBM model ...")
gbm <- trainGBM(training)

stopCluster(cl)

# MEASURE TIMES
message("Measuring average times...")

times <- list(nb = vector(), gbm = vector(), rf = vector())

for (i in seq(1, nrow(testing))) {
  time_nb <- system.time({
    pp = representFeaturesAsFactors(testing[i,], margin = c(1,2)); 
    predict(nb, pp)})[2]
  time_gbm <- system.time(predict(gbm, testing[i,]))[2]
  time_rf <- system.time(predict(rf, testing[i,]))[2]
    
  times$nb <- c(times$nb, time_nb)
  times$gbm <- c(times$gbm, time_gbm)
  times$rf <- c(times$rf, time_rf)
}
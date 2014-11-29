library(dplyr)
library(tm)
library(caret)

source("getRecords.R")
source("naiveBayes.R")

set.seed(424232)

df <- prepareDataset()

message("Creating train, test datasets ...")
inTrain <- createDataPartition(df$CLASS, p = 0.8, list = FALSE)

training <- df[inTrain,]
testing <- df[-inTrain,]

message("Training Naive Bayes model ...")
nb <- naiveBayes(training)


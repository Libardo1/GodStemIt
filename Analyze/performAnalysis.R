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
source("svm.R")

set.seed(424232)
registerDoParallel(cores = 2)

df <- prepareDataset()

message("Creating train, test datasets ...")
inTrain <- createDataPartition(df$CLASS, p = 0.8, list = FALSE)

training <- df[inTrain,]
testing <- df[-inTrain,]

message("Training Naive Bayes model ...")
nb <- trainNaiveBayes(training)

#message("Training Random Forest model ...")
#rf <- trainRandomForest(training)

#message("Training linear SVM model ...")
#svm <- trainLinearSVM(training)

#message("Training RBF SVM model ...")
#svm <- trainRadialBasisSVM(training)
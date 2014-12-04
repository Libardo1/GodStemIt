trainLinearSVM <- function(dataset) {
  storedModelFile <- "model/svm_linear.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading linear SVM model from file")
    load(storedModelFile)
  } else {
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction =  twoClassSummary,
                         verboseIter = TRUE)
    
    grid_svm <- expand.grid(.C = logseq(2^-15, 2^3, 10))
    
    svm <- train(CLASS ~ ., data = dataset, method = "svmLinear", preProcess = c("center", "scale"),
                metric = "ROC", trControl = ctrl, tuneGrid = grid_svm, verbose = TRUE)
    
    message("Saving model to file ...")
    save(svm, file = storedModelFile)
  }
  
  return (svm)
}

trainRadialBasisSVM <- function(dataset) {
  storedModelFile <- "model/svm_rbf.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading RBF SVM model from file")
    load(storedModelFile)
  } else {
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction =  twoClassSummary,
                         verboseIter = TRUE)
    
    grid_svm <- expand.grid(.C = logseq(2^-15, 2^3, 7),
                            .sigma = logseq(2^-5, 2^15, 7))
    
    svm <- train(CLASS ~ ., data = dataset, method = "svmRadial", preProcess = c("center", "scale"),
                 metric = "ROC", trControl = ctrl, tuneGrid = grid_svm, verbose = TRUE)
    
    message("Saving model to file ...")
    save(svm, file = storedModelFile)
  }
  
  return (svm)
}
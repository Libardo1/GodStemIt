trainRandomForest <- function(dataset) {
  storedModelFile <- "model/randomForest.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading Random Forest model from file")
    load(storedModelFile)
  } else {
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction = twoClassSummary,
                         verboseIter = TRUE)
    
    ftrs <- logseq(32, 96, 5)
    ftrs <- as.integer(ftrs)
    grid_rf <- expand.grid(.mtry = ftrs)
    
    rf <- train(CLASS ~ ., data = dataset, method = "parRF", verbose = TRUE,
                metric = "ROC", trControl = ctrl, tuneGrid = grid_rf)
    
    message("Saving model to file ...")
    save(rf, file = storedModelFile)
  }
  
  return (rf)
}
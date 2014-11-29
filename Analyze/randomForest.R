trainRandomForest <- function(dataset) {
  storedModelFile <- "model/randomForest.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading Random Forest model from file")
    load(storedModelFile)
  } else {
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction = twoClassSummary,
                         verboseIter = TRUE)
    
    grid_rf <- expand.grid(.mtry = c(64, 128, 256))
    
    rf <- train(CLASS ~ ., data = dataset, method = "parRF",
                metric = "ROC", trControl = ctrl, tuneGrid = grid_rf)
    
    message("Saving model to file ...")
    save(rf, file = storedModelFile)
  }
  
  return (rf)
}
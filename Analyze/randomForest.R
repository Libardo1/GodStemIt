randomForest <- function(dataset) {
  ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "oneSE", classProbs = TRUE)
  grid <- expand.grid(.mtry = c(2, 4, 8, 16))
  
  rf <- train(CLASS ~ ., data = dataset, method = "rf",
              metric = "ROC", trControl = ctrl, tuneGrid = grid)
  
  return (rf)
}
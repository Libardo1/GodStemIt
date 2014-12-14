trainNaiveBayes <- function(dataset) {
  storedModelFile <- "model/naiveBayes.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading Naive Bayes model from file")
    load(storedModelFile)
  } else {   
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction = twoClassSummary,
                         verboseIter = TRUE)
    
    grid <- expand.grid(.fL = 0, .usekernel = FALSE)
    
    dataset <- representFeaturesAsFactors(dataset)
    
    nb <- train(CLASS ~ ., data = dataset, method = "nb", verbose = TRUE,
                metric = "ROC", trControl = ctrl, tuneGrid = grid)
    
    message("Saving model to file ...")
    save(nb, file = storedModelFile)
  }
  
  return (nb)
}
  
representFeaturesAsFactors <- function(dataset, margin = 2) {
  classes <- dataset$CLASS
  matrix_data <- apply(dataset[, -which(names(dataset) %in% c("CLASS"))], 
                       MARGIN = margin, 
                       function(x) ifelse(x > 0, 1, 0)) # Exclude CLASS column
  
  dataset_tmp <- data.frame(matrix_data)
  dataset_tmp <- lapply(dataset_tmp, factor, levels = c(0,1), labels = c("NO", "YES"))
  
  dataset <- data.frame(dataset_tmp)
  dataset$CLASS <- classes
  
  return(dataset)
}
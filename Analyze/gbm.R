trainGBM <- function(dataset) {
  storedModelFile <- "model/gbm_extended2.RModel"
  
  if (file.exists(storedModelFile)) {
    message("Loading GBM model from file")
    load(storedModelFile)
  } else {
    ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "best", 
                         classProbs = TRUE, summaryFunction =  twoClassSummary,
                         verboseIter = TRUE, allowParallel = TRUE)
    
    gbm_grid <-  expand.grid(interaction.depth = c(1, 2, 3), # complexity of the tree
                            n.trees = as.integer(logseq(1,500,8)), # number of iterations
                            shrinkage = 0.1) # learning rate
    
    gbm <- train(CLASS ~ ., data = dataset, method = "gbm",
                 metric = "ROC", trControl = ctrl, tuneGrid = gbm_grid, verbose = TRUE)
    
    message("Saving model to file ...")
    save(gbm, file = storedModelFile)
  }
  
  return (gbm)
}
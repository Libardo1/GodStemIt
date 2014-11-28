library(dplyr)
library(tm)
library(caret)

getDataSet <- function() {
  ds <- list()
  
  message("Connecting to MySQL database...")
  sa_db <- src_mysql("sentiment_analysis", host = "localhost", port = 3306, password = "root")
  
  message("Getting all data...")
  sa_table <- tbl(sa_db, "entry")
  
  dbSendQuery(sa_db$con, "set names utf8")
  
  message("Getting all sentences with label 2.0  ...")
  sentences_20 <- sa_table %>%
    select(processed, class) %>%
    filter(class == "2.0") %>%
    collect()
  
  message("Getting all sentences with label 5.0 ...")
  sentences_50 <- sa_table %>%
    select(processed, class) %>%
    filter(class == "5.0") %>%
    collect()
  
  message("Shuffle sentences with label 5.0 ...")
  sentences_50 <- sentences_50[order(runif(nrow(sentences_50))),]
  
  message("Balance the datasets ...")
  sentences_50 <- sentences_50[1:nrow(sentences_20),]
  sentences <- rbind(sentences_20, sentences_50)
    
  # Declare polish stop-words
  stopWords <- c("być", "się", "ale", "asortyment", "była", "były", "cena", "chcieć", "coś", 
                 "czas", "czy", "dla", "jak", "jednak", "jeszcze", "już", "kupić", "lokal", 
                 "miąć", "mieć", "miejsce", "obsługa", "obsługiwać", "oferta", "pracownik", 
                 "produkt", "personel", "polecać", "półka", "pracownica", "pracownik", "tak", 
                 "taki", "tam", "ten", "też", "towar", "tylko", "tym", "wejście", "więc", "wszystek", 
                 "wszystko", "wybór", "wybrać", "zakup", "zakupy", "znajdować", "znaleźć",
                 "kasa", "klient", "sklep", "przy", "który", "oraz", "pani", "raz", "również", "sam")
  
  message("Creating corpus object from sentences ...")
  corpus <- Corpus(VectorSource(sentences$processed))
  
  message("Removing white spaces ...")
  corpus <- tm_map(corpus, stripWhitespace)
  
  message("Removing stop words ...")
  corpus <- tm_map(corpus, removeWords, stopWords)
  
  message("Creating 'Document-Term-Matrix' representation ...")
  dtm <- DocumentTermMatrix(corpus, 
                            control = list(bounds = list(global = c(20, Inf))))
  
  # At this point we have 9 041 terms with 100% sparsity
  # 90% sparsity -> 20 terms
  # 95% sparsity -> 106 terms
  # 98% sparsity -> 400 terms
  # 99% sparsity -> 784 terms
  # 99,3% sparsity -> 1 093 terms (44,3 Mb)
    
  message("Transforming dtm to 99,3% sparsity level...")
  dtm <- removeSparseTerms(dtm, 0.993)
    
  message("Transforming dtm to data-frame ...")
  dataFrame <- as.data.frame(as.matrix(dtm))
  dataFrame$CLASS <- factor(sentences$class, levels = c(2.0, 5.0), labels = c("NEG", "POS"))  
  
  message("Done ...")
  return (dataFrame);
}

naiveBayes <- function(dataset) {
  ctrl <- trainControl(method = "cv", number = 2, selectionFunction = "oneSE", 
                       classProbs = TRUE, summaryFunction = twoClassSummary)
  
  grid <- expand.grid(.fL = 1,
                      .usekernel = c(TRUE,FALSE))
  
  # Represent features as factor
  classes <- dataset$CLASS
  matrix_data <- apply(dataset[, -which(names(dataset) %in% c("CLASS"))], 
                       MARGIN = 2, 
                       function(x) ifelse(x > 0, 1, 0)) # Exclude CLASS column
  
  dataset_tmp <- data.frame(matrix_data)
  dataset_tmp <- lapply(dataset_tmp, factor, levels = c(0,1), labels = c("NO", "YES"))
  
  dataset <- data.frame(dataset_tmp)
  dataset$CLASS <- classes
  
  nb <- train(CLASS ~ ., data = dataset, method = "nb",
              metric = "Kappa", trControl = ctrl, tuneGrid = grid)
  
  return (nb)
}

randomForest <- function(dataset) {
  ctrl <- trainControl(method = "cv", number = 10, selectionFunction = "oneSE", classProbs = TRUE)
  grid <- expand.grid(.mtry = c(2, 4, 8, 16))
  
  rf <- train(CLASS ~ ., data = dataset, method = "rf",
              metric = "ROC", trControl = ctrl, tuneGrid = grid)
  
  return (rf)
}

## SCRIPT STARTS HERE ##
setwd("/Users/khozzy/R/Sentiment Analysis/")
dataSetObjectFile <- "sentences.RData"
set.seed(424232)
message("Checking if file with data exists...")

if (file.exists(dataSetObjectFile)) {
  message("Loading data from file")
  load(dataSetObjectFile)
} else {
  df <- getDataSet()
  save(df, file = dataSetObjectFile)
}

# get Naive Bayes model
#small <- df[12500:21500,]
#model <- naiveBayes(small)
#model <- naiveBayes(df)

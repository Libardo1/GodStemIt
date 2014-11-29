prepareDataset <- function() {
  dataSetObjectFile <- "sentences.RData"
  message("Checking if file with data exists...")
  
  if (file.exists(dataSetObjectFile)) {
    message("Loading data from file")
    load(dataSetObjectFile)
  } else {    
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
    
    message("Balancing the datasets ...")
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
      
    message("Transforming dtm to 99,3% sparsity level ...")
    dtm <- removeSparseTerms(dtm, 0.993)
      
    message("Transforming dtm to data-frame ...")
    dataFrame <- as.data.frame(as.matrix(dtm))
    dataFrame$CLASS <- factor(sentences$class, levels = c(2.0, 5.0), labels = c("NEG", "POS"))  
    
    message("Saving data-frame to file ...")
    save(dataFrame, file = dataSetObjectFile)
  }
  
  message("Done ...")
  return (dataFrame);
}
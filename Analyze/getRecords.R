prepareDataset <- function() {
  dataSetObjectFile <- "data/sentences.RData"
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
    
    message("Getting all sentences with label 0.5  ...")
    sentences_bad <- sa_table %>%
      select(processed, class) %>%
      filter(class == "0.5") %>%
      collect()
    
    message("Getting all sentences with label 5.0 ...")
    sentences_good <- sa_table %>%
      select(processed, class) %>%
      filter(class == "5.0") %>%
      collect()
    
    message("Shuffle sentences with label 5.0 ...")
    sentences_good <- sentences_good[order(runif(nrow(sentences_good))),]
    
    message("Balancing the datasets ...")
    sentences_good <- sentences_good[1:nrow(sentences_bad),]
    sentences <- rbind(sentences_bad, sentences_good)
      
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
    dataFrame$CLASS <- factor(sentences$class, levels = c(0.5, 5.0), labels = c("NEG", "POS"))  
    
    message("Saving data-frame to file ...")
    save(dataFrame, file = dataSetObjectFile)
  }
  
  message("Done ...")
  return (dataFrame);
}
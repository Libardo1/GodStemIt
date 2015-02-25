if (!exists("prepareSentence")) {
    message("Loading prepareSentence function from file ...")

    library(tm)

    prepareSentence <- function(rawSentence) {

      corpus <- Corpus(VectorSource(as.vector(rawSentence)))
      corpus <- tm_map(corpus, stripWhitespace)
      corpus <- tm_map(corpus, removePunctuation)

      dtm <- DocumentTermMatrix(corpus)
      df <- as.data.frame(as.matrix(dtm))

      pattern <- data.frame(matrix(ncol = length(all.terms), nrow = 1))
      colnames(pattern) <- all.terms

      new.sentence <- merge(pattern, df, all.y = T)
      new.sentence <- new.sentence[, all.terms]
      new.sentence[is.na(new.sentence)] <- 0

      return(new.sentence)
    }
}

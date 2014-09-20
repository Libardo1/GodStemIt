library(RMySQL)
library(tm)
library(wordcloud)

dbh <- dbConnect(MySQL(), 
                 user = 'root', 
                 password = 'root', 
                 dbname = 'sentiment_analysis',
                 host = 'localhost')

dbSendQuery(dbh, "SET NAMES utf8")

rs <- dbSendQuery(dbh, "SELECT processed, class from entry WHERE processed IS NOT NULL")
data <- fetch(rs, n=-1)

# Analysis
stopwords = c("być", "się")
sentiment_corpus <- Corpus(VectorSource(data$processed))

corpus_clean <- tm_map(sentiment_corpus, stripWhitespace)
corpus_clean <- tm_map(corpus_clean, removeWords, stopwords)
  
sentiment_dtm <- DocumentTermMatrix(corpus_clean)

wordcloud(corpus_clean)
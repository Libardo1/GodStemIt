if (exists("myFunc")) {
  message("myFunc exists ... Using it")
} else {
  message("No myFunc... Creating one...")
  myFunc <- function() {
    a <- 2
    b <- 5
    
    return (a+b)
  }  
}


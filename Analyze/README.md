# Analysis
Processing collected opinions data with R language.

##Dependencies

    install.packages("caret", dependencies = TRUE)
    install.packages("dplyr", dependencies = TRUE)
    install.packages("tm", dependencies = TRUE)
    install.packages("foreach", dependencies = TRUE)
    install.packages("doParallel", dependencies = TRUE)

##Desciption
###Pre-processing opinions
After performing stemming on the collected sentences additional pre-precessing is required. The final data frame will
consists of extreme negative and positive cases only (classes 2.0 and 5.0). There is nearly 9x more positive opinions
so this number will be reduced to the number of negative ones. By doing this it will be faster for classifier to learn and
the classes will be balanced (accuracy metrics for evaluating results will be useful).

After retrieving filtered opinions they will be represented in a matrix form. Each row will represent opinion, and column will
be a term so each cell will contain an information about how many times a certain term was present in opinion.

Initial number of terms in very huge (about 320 000 terms) which causes that almost every cell in the matrix corresponds to zero.
Therefore most frequent words (stop-words) were removed and additionally sparsity was reduced to 99,3%. After this steps the number
of terms was about 1300.

Finally all data was stored in data-frame object, with additional column (`CLASS`) storing the information if the row (opinion) is
positive (`POS`) or negative (`NEG`).

Script returning processed data frame which can be used to feed training algorithms can be found [here] (getRecords.R).
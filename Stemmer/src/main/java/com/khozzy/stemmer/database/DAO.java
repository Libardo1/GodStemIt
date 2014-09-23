package com.khozzy.stemmer.database;

import com.khozzy.stemmer.domain.Sentence;

import java.util.Set;

public interface DAO {

    Set<Sentence> getNotProcessedStatements(int limit);
    int countRemainingToProcess();
    void updateSentence(int id, String processed, boolean error);

}

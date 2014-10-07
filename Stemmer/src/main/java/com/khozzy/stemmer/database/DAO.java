package com.khozzy.stemmer.database;

import com.khozzy.stemmer.domain.Sentence;

import java.util.Set;

public interface DAO {

    Set<Sentence> getNotProcessedStatements();
    void updateSentence(Sentence sentence);
}

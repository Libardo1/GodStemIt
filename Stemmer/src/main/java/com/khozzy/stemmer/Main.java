package com.khozzy.stemmer;

import com.khozzy.stemmer.database.DAO;
import com.khozzy.stemmer.database.MysqlDAO;
import com.khozzy.stemmer.domain.Sentence;
import com.khozzy.stemmer.stemming.PolishStemming;

import java.util.Set;

public class Main {

    private static final int BATCH_SIZE = 5;

    public static void main(String[] args) {
        final DAO dao = new MysqlDAO();
        final int total = dao.countRemainingToProcess();
        long start, time;

        Set<Sentence> sentences;

        System.out.println(String.format("Ilość zdań do przetworzenia: %d", total));

        start = System.currentTimeMillis();

        do {
            sentences = dao.getNotProcessedStatements(BATCH_SIZE);

            sentences
                    .parallelStream()
                    .forEach(s -> {
                        final String processed = PolishStemming.process(s.getOriginal());

                        if (processed != null) {
                            dao.updateSentence(s.getId(), processed, false);
                        } else {
                            dao.updateSentence(s.getId(), null, true);
                        }
                    });
        } while (sentences.size() > 0);

        time = System.currentTimeMillis() - start;

        System.out.println(String.format("\nZakończono.\nCałkowity czas: %d ms", time));
    }
}

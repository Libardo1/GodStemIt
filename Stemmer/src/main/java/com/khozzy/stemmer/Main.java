package com.khozzy.stemmer;

import com.khozzy.stemmer.database.DAO;
import com.khozzy.stemmer.database.mysql.MysqlDAO;
import com.khozzy.stemmer.domain.Sentence;
import com.khozzy.stemmer.stemming.PolishStemming;
import org.apache.log4j.Logger;

import java.util.Set;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);
    private static final int BATCH_SIZE = 5;

    public static void main(String[] args) {
        final DAO dao = new MysqlDAO();
        final int total = dao.countRemainingToProcess();
        long start, time;

        Set<Sentence> sentences;

        logger.info(String.format("Ilość zdań do przetworzenia: %d", total));
        logger.info(String.format("Liczba dostępnych procesorów: %d", Runtime.getRuntime().availableProcessors()));

        start = System.currentTimeMillis();

        do {
            sentences = dao.getNotProcessedStatements(BATCH_SIZE);

            sentences
                    .parallelStream()
                    .forEach(s -> {
                        logger.debug(String.format("[%d] Watek zarezerwowany ", s.getId()));
                        final Sentence processed = PolishStemming.process(s);

                        if (processed != null) {
                            dao.updateSentence(s.getId(), s.getProcessed(), false);
                        } else {
                            dao.updateSentence(s.getId(), null, true);
                        }
                        logger.debug(String.format("[%d] Watek zwolniony ", s.getId()));
                    });

        } while (sentences.size() > 0);

        time = System.currentTimeMillis() - start;

        logger.info(String.format("Zakończono. Całkowity czas: %d ms", time));
    }
}

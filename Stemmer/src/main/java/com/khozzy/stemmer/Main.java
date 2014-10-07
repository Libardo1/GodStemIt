package com.khozzy.stemmer;

import com.khozzy.stemmer.database.DAO;
import com.khozzy.stemmer.database.mysql.MysqlDAO;
import com.khozzy.stemmer.domain.Sentence;
import com.khozzy.stemmer.stemming.PolishStemming;
import org.apache.log4j.Logger;

import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static final Logger logger = Logger.getLogger(Main.class);

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        final DAO dao = new MysqlDAO();
        long start, time;

        Set<Sentence> sentences = dao.getNotProcessedStatements();
        final int total = sentences.size();

        logger.info(String.format("Ilość zdań do przetworzenia: %d", total));
        logger.info(String.format("Liczba dostępnych procesorów: %d", Runtime.getRuntime().availableProcessors()));

        start = System.currentTimeMillis();

        CountDownLatch latch = new CountDownLatch(total);
        ExecutorService executor = Executors.newFixedThreadPool(16);
        sentences.forEach(s -> executor.submit(new PolishStemming(dao, latch, s)));

        executor.shutdown();
        latch.await();

        time = System.currentTimeMillis() - start;

        logger.info(String.format("Zakończono. Całkowity czas: %.3f s", (double) time/1000));
    }
}

package com.khozzy.stemmer.stemming;

import com.khozzy.stemmer.database.DAO;
import com.khozzy.stemmer.domain.Sentence;
import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PolishStemming implements Runnable {

    private final static Dictionary dictionary = Dictionary.getForLanguage("pl");
    private final static Pattern UNDESIRABLES = Pattern.compile("[,.;!?(){}\\[\\]<>%]");
    private static final Logger logger = Logger.getLogger(PolishStemming.class);

    private DAO dao;
    private CountDownLatch latch;
    private Sentence sentence;

    public PolishStemming(DAO dao, CountDownLatch latch, Sentence sentence) {
        this.dao = dao;
        this.latch = latch;
        this.sentence = sentence;
    }

    @Override
    public void run() {
        logger.debug("[START] Stemming zdania o id: " + sentence.getId());

        StringBuilder processed = new StringBuilder();
        final DictionaryLookup dl = new DictionaryLookup(dictionary);

        final String[] words = sentence
                .getOriginal()
                .toLowerCase()
                .split(" ");

        Stream
                .of(words)
                .forEach(w -> {
                    w = UNDESIRABLES.matcher(w).replaceAll("");

                    List<WordData> wordList = dl.lookup(w);

                    if (!wordList.isEmpty()) {
                        processed.append(wordList.get(0).getStem());
                        processed.append(" ");
                    }
                });

        sentence.setProcessed(processed.toString().trim());

        dao.updateSentence(sentence);

        latch.countDown();

        logger.debug("[STOP] Stemming zdania o id: " + sentence.getId());
    }
}

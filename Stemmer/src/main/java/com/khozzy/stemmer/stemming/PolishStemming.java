package com.khozzy.stemmer.stemming;

import com.khozzy.stemmer.domain.Sentence;
import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;
import org.apache.log4j.Logger;

import java.lang.Object;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PolishStemming {

    private final static Object LOCK = new Object() {};
    private final static Pattern UNDESIRABLES = Pattern.compile("[,.;!?(){}\\[\\]<>%]");
    private static final Logger logger = Logger.getLogger(PolishStemming.class);

    private final static Dictionary polish = Dictionary.getForLanguage("pl");
    private final static DictionaryLookup dl = new DictionaryLookup(polish);

    public static Sentence process(final Sentence sentence) {
        synchronized (LOCK) {
            logger.debug("[START] Stemming zdania o id: " + sentence.getId());

            StringBuilder processed = new StringBuilder();

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

            logger.debug("[STOP] Stemming zdania o id: " + sentence.getId());

            return sentence;
        }
    }
}

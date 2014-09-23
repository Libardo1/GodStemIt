package com.khozzy.stemmer.stemming;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;

import java.lang.Object;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class PolishStemming {

    private final static Object LOCK = new Object() {};
    private final static Pattern UNDESIRABLES = Pattern.compile("[,.;!?(){}\\[\\]<>%]");

    private final static Dictionary polish = Dictionary.getForLanguage("pl");
    private final static DictionaryLookup dl = new DictionaryLookup(polish);

    public static String process(final String sentence) {
        synchronized (LOCK) {
            StringBuilder processed = new StringBuilder();

            final String[] words = sentence
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

            return processed.toString().trim();
        }
    }
}

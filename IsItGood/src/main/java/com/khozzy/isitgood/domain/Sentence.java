package com.khozzy.isitgood.domain;

import morfologik.stemming.Dictionary;
import morfologik.stemming.DictionaryLookup;
import morfologik.stemming.WordData;

import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class Sentence {

    private final static Dictionary dictionary = Dictionary.getForLanguage("pl");
    private final static Pattern UNDESIRABLES = Pattern.compile("[,.;!?(){}\\[\\]<>%]");

    protected String original;
    protected String stemmed = null;
    protected double posProb = 0.0;

    public Sentence(String original) {
        this.original = original;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public String getStemmed() {
        return stemmed;
    }

    public void setStemmed(String stemmed) {
        this.stemmed = stemmed;
    }

    public double getPosProb() {
        return posProb;
    }

    public void setPosProb(double posProb) {
        this.posProb = posProb;
    }

    public void stem() {
        StringBuilder processed = new StringBuilder();
        final DictionaryLookup dl = new DictionaryLookup(dictionary);

        final String[] words = original.toLowerCase().split(" ");

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

        stemmed = processed.toString().trim();
    }
}
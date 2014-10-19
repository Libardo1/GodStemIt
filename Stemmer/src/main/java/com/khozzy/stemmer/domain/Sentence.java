package com.khozzy.stemmer.domain;

public class Sentence {

    protected int id;
    protected String original;
    protected String processed = null;
    protected int clazz;

    public Sentence(
            int id,
            String original,
            int clazz) {

        this.id = id;
        this.original = original;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public String getOriginal() {
        return original;
    }

    public String getProcessed() {
        return processed;
    }

    public void setProcessed(String processed) {
        this.processed = processed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Sentence sentence = (Sentence) o;

        if (clazz != sentence.clazz) return false;
        if (id != sentence.id) return false;
        if (!original.equals(sentence.original)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + original.hashCode();
        result = 31 * result + clazz;
        return result;
    }

    @Override
    public String toString() {
        return "Sentence{" +
                "id=" + id +
                '}';
    }
}

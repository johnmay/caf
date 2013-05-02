package uk.ac.ebi.caf.component.complete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.zip.GZIPInputStream;

/**
 * Ternary search tree structure for finding words with a give prefix. Searches
 * are case insensitive.
 *
 * @author John May */
public class PrefixSearch {

    private Node root;

    /**
     * Create a new prefix search for the set of strings.
     *
     * @param strs collection of strings
     */
    private PrefixSearch(Collection<String> strs) {
        for (String str : strs)
            add(str);
    }

    /**
     * Add a word to the tree.
     *
     * @param str a word
     */
    public void add(String str) {
        if (str != null && !str.isEmpty())
            root = add(root, str, 0);
    }

    /**
     * Find words starting with the given prefix.
     *
     * @param prefix a prefix
     * @return words starting with the prefix
     */
    public Collection<String> startsWith(String prefix) {
        if (prefix == null || prefix.isEmpty())
            return Collections.emptySet();
        Node n = get(root, prefix, 0);
        List<String> words = new ArrayList<String>();
        if(n != null) collect(n.eq, words, prefix);
        return words;
    }

    private Node add(Node n, String org, int d) {
        char c = Character.toLowerCase(org.charAt(d));
        if(n == null) n = new Node(c);
        if      (c < n.c)              n.lo = add(n.lo, org, d);
        else if (c > n.c)              n.hi = add(n.hi, org, d);
        else if (d < org.length() - 1) n.eq = add(n.eq, org, d + 1);
        else n.word = org;
        return n;
    }

    private Node get(Node n, String str, int d) {
        if(n == null) return null;
        char c = Character.toLowerCase(str.charAt(d));
        if      (c < n.c) return get(n.lo, str, d);
        else if (c > n.c) return get(n.hi, str, d);
        else if (d < str.length() - 1) return get(n.eq, str, d + 1);
        else return n;
    }

    private void collect(Node n, Collection<String> values, String prefix) {
        if(n == null) return;
        if(n.word != null) values.add(n.word);
        collect(n.lo, values, prefix);
        collect(n.eq, values, prefix + n.c);
        collect(n.hi, values, prefix);
    }

    /**
     * Build a tree from a stream. The stream is lines, each line is added to
     * the tree.
     *
     * @param in input stream
     * @return prefix search
     * @throws IOException low-level io
     */
    public static PrefixSearch fromStream(InputStream in) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            List<String> words = new ArrayList<String>();
            String line = null;
            while ((line = reader.readLine()) != null) {
                words.add(line);
            }
            return forStrings(words);
        } finally {
            if (in != null) in.close();
        }
    }

    /**
     * Build a tree from a stream compressed with GZip. The stream is lines,
     * each line is added to the tree.
     *
     * @param in compressed input stream
     * @return prefix search
     * @throws IOException low-level io
     */
    public static PrefixSearch fromGZStream(InputStream in) throws IOException {
        try {
            return fromStream(new GZIPInputStream(in));
        } finally {
            if (in != null) in.close();
        }
    }

    /**
     * Create a prefix search for a list of strings. The strings are first
     * shuffled to avoid worst case insertion.
     *
     * @param xs list of strings
     * @return prefix search
     */
    public static PrefixSearch forStrings(List<String> xs) {
        // sorted order is worst
        Collections.shuffle(xs);
        return new PrefixSearch(xs);
    }

    /**
     * A prefix search for english words.
     *
     * @return prefix search for english words
     */
    public static PrefixSearch englishWords() {
        try {
            return fromGZStream(PrefixSearch.class.getResourceAsStream(ENGLISH_WORDS));
        } catch (IOException e) {
            throw new IllegalStateException(ENGLISH_WORDS + " could not be loaded");
        }
    }

    private static final String ENGLISH_WORDS = "wordsEn.txt.gz";

    private static class Node {
        private Node lo, eq, hi;
        private final char c;
        private String word;

        private Node(char c) {
            this.c = c;
        }
    }
}

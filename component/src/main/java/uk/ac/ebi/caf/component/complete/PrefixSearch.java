package uk.ac.ebi.caf.component.complete;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.zip.GZIPInputStream;

/**
 * Ternary search tree structure for finding words with a give prefix.
 * @author John May */
public class PrefixSearch {

    private Node root;

    /**
     * Create a new prefix search for the set of strings.
     *
     * @param strs collection of strings
     */
    public PrefixSearch(Collection<String> strs) {
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
            root = add(root, str.toLowerCase(Locale.ENGLISH), str, 0);
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
        Node n = get(root, prefix.toLowerCase(Locale.ENGLISH), 0);
        List<String> words = new ArrayList<String>();
        if(n != null) collect(n.middle, words, prefix);
        return words;
    }

    private Node add(Node n, String key, String str, int d) {
        char c = key.charAt(d);
        if(n == null) n = new Node(c);
        if      (c < n.c)              n.left   = add(n.left, key, str, d);
        else if (c > n.c)              n.right  = add(n.right, key, str, d);
        else if (d < str.length() - 1) n.middle = add(n.middle, key, str, d + 1);
        else n.word = str;
        return n;
    }

    private Node get(Node n, String str, int d) {
        if(n == null) return null;
        char c = str.charAt(d);
        if      (c < n.c) return get(n.left, str, d);
        else if (c > n.c) return get(n.right, str, d);
        else if (d < str.length() - 1) return get(n.middle, str, d + 1);
        else return n;
    }

    private void collect(Node n, Collection<String> values, String prefix) {
        if(n == null) return;
        if(n.word != null) values.add(n.word);
        collect(n.left,   values, prefix);
        collect(n.middle, values, prefix + n.c);
        collect(n.right,  values, prefix);
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
            // sorted order is worst
            Collections.shuffle(words);
            return new PrefixSearch(words);
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
        private Node left, middle, right;
        private final char c;
        private String word;

        private Node(char c) {
            this.c = c;
        }
    }
}

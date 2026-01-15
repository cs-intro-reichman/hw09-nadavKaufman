import java.util.HashMap;
import java.util.Random;

public class LanguageModel {

    // The map of this model.
    // Maps windows to lists of charachter data objects.
    HashMap<String, List> map;

    // The window length used in this model.
    int k;

    // The random number generator used by this model.
    private Random rng;

    /**
     * Constructs a language model with the given window length and a given
     * seed value. Generating texts from this model multiple times with the
     * same seed value will produce the same random texts. Good for debugging.
     */
    public LanguageModel(int k, int seed) {
        this.k = k;
        rng = new Random(seed);
        map = new HashMap<String, List>();
    }

    /**
     * Constructs a language model with the given window length.
     * Generating texts from this model multiple times will produce
     * different random texts. Good for production.
     */
    public LanguageModel(int k) {
        this.k = k;
        rng = new Random();
        map = new HashMap<String, List>();
    }

    /** Builds a language model from the text in the given file (the corpus). */
    public void train(String fileName) {
        String win = "";
        char ch;
        In in = new In(fileName);

        // Reads the first window from the file.
        for (int i = 0; i < k && !in.isEmpty(); i++) {
            win += in.readChar();
        }

        // Processes the entire text, one character at a time.
        while (!in.isEmpty()) {
            ch = in.readChar();

            if (ch == '\r') {
                continue;
            }

            List list = map.get(win);
            if (list == null) {
                list = new List();
                map.put(win, list);
            }
            list.update(ch);
            win = win.substring(1) + ch;
        }

        // Computes and set the p and cp fields of all the linked list objects in the
        // map.
        for (List list : map.values()) {
            calculateProbabilities(list);
        }
    }

    // Computes and sets the probabilities (p and cp fields) of all the
    // characters in the given list. */
    void calculateProbabilities(List list) {
        int total = 0;
        ListIterator it = list.listIterator(0);
        while (it.hasNext()) {
            CharData cd = it.next();
            total += cd.count;
        }

        ListIterator it2 = list.listIterator(0);
        double prev = 0.0;
        while (it2.hasNext()) {
            CharData cd = it2.next();
            cd.p = (double) cd.count / total;
            cd.cp = prev + cd.p;
            prev = cd.cp;
        }
    }

    // Returns a random character from the given probabilities list.
    char getRandomChar(List list) {
        double r = rng.nextDouble();
        ListIterator it = list.listIterator(0);
        while (it.hasNext()) {
            CharData cd = it.next();
            if (cd.cp > r) {
                return cd.chr;
            }
        }
        // Retruns the last character in the list in case the random number is very
        // close to 1.
        return list.get(list.getSize() - 1).chr;
    }

    /**
     * Generates a random text, based on the probabilities that were learned during
     * training.
     * 
     * @param initialText     - text to start with. If initialText's last substring
     *                        of size numberOfLetters
     *                        doesn't appear as a key in Map, we generate no text
     *                        and return only the initial text.
     * @param numberOfLetters - the size of text to generate
     * @return the generated text
     */
    public String generate(String initialText, int textLength) {
        if (initialText.length() < k) {
            return initialText;
        }

        StringBuilder out = new StringBuilder(initialText);
        while (out.length() < textLength || out.charAt(out.length() - 1) != ' ') {
            String win = out.substring(out.length() - k);
            List list = map.get(win);
            if (list == null) {
                break;
            }
            char next = getRandomChar(list);
            out.append(next);
        }
        return out.toString();
    }

    /** Returns a string representing the map of this language model. */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (String key : map.keySet()) {
            List list = map.get(key);
            sb.append(key + " : " + list + "\n");
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        String start = args[1];
        int len = Integer.parseInt(args[2]);
        Boolean isRandom = args[3].equals("random");
        String fileName = args[4];

        // Create the LanguageModel object
        LanguageModel lm;
        if (isRandom) {
            lm = new LanguageModel(k);
        } else {
            lm = new LanguageModel(k, 20);
        }

        // Trains the model, creating the map.
        lm.train(fileName);

        // Generating text, and prints it.
        System.out.println(lm.generate(start, len));
    }
}
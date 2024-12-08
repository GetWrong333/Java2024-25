import java.util.*;

public class StringCompressor extends Compression {
    private String originalText;
    private Map<String, Integer> wordFrequency;
    private List<String> dictionary;
    private String compressedText;

    public StringCompressor() {
        originalText = "";
        wordFrequency = new HashMap<>();
        dictionary = new ArrayList<>();
        compressedText = "";
    }

    @Override
    public void input(String input) {
        originalText = input;
        wordFrequency.clear();
        dictionary.clear();
        compressedText = "";

        // Create a cleaned version of the text for processing
        String[] wordsWithPunctuation = input.split("\\s+");
        Map<String, String> cleanToOriginal = new HashMap<>();

        for (String word : wordsWithPunctuation) {
            String cleanedWord = word.replaceAll("[.,:;\"!()]", "");
            cleanToOriginal.put(cleanedWord, word);
            wordFrequency.put(cleanedWord, wordFrequency.getOrDefault(cleanedWord, 0) + 1);
        }

        // Find the best dictionary and compress
        findBestDictionaryAndCompress(wordsWithPunctuation, cleanToOriginal);
    }

    private void findBestDictionaryAndCompress(String[] wordsWithPunctuation, Map<String, String> cleanToOriginal) {
        List<Map.Entry<String, Integer>> sortedWords = new ArrayList<>(wordFrequency.entrySet());
        sortedWords.sort((a, b) -> {
            int byFrequency = Integer.compare(b.getValue(), a.getValue());
            if (byFrequency != 0) return byFrequency;
            return Integer.compare(b.getKey().length(), a.getKey().length());
        });

        int[] validSizes = {2, 4, 8, 16, 32, 64};
        int minCost = Integer.MAX_VALUE;

        for (int size : validSizes) {
            if (size > sortedWords.size()) break;

            List<String> currentDictionary = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                currentDictionary.add(sortedWords.get(i).getKey());
            }

            Map<String, String> wordToBinary = createBinaryMap(currentDictionary);
            String encoded = encodeText(wordsWithPunctuation, wordToBinary, cleanToOriginal);

            int dictionaryCost = currentDictionary.stream().mapToInt(String::length).sum();
            int totalCost = encoded.length() + dictionaryCost;

            if (totalCost < minCost) {
                minCost = totalCost;
                dictionary = currentDictionary;
                compressedText = encoded;
            }
        }

        if (minCost >= originalText.length()) {
            dictionary.clear();
            compressedText = originalText;
        }
    }

    private Map<String, String> createBinaryMap(List<String> dictionary) {
        Map<String, String> mapping = new HashMap<>();
        int bits = (int) Math.ceil(Math.log(dictionary.size()) / Math.log(2));

        for (int i = 0; i < dictionary.size(); i++) {
            String binary = Integer.toBinaryString(i);
            while (binary.length() < bits) {
                binary = "0" + binary;
            }
            mapping.put(dictionary.get(i), binary);
        }

        return mapping;
    }

    private String encodeText(String[] wordsWithPunctuation, Map<String, String> wordToBinary, Map<String, String> cleanToOriginal) {
        StringBuilder encodedText = new StringBuilder();

        for (String word : wordsWithPunctuation) {
            String cleanedWord = word.replaceAll("[.,:;\"!()]", "");
            if (wordToBinary.containsKey(cleanedWord)) {
                encodedText.append(word.replace(cleanedWord, wordToBinary.get(cleanedWord))).append(" ");
            } else {
                encodedText.append(word).append(" ");
            }
        }

        return encodedText.toString().trim();
    }

    @Override
    public Map<String, Integer> histogram() {
        return wordFrequency;
    }

    @Override
    public List<String> code() {
        return dictionary;
    }

    @Override
    public String output() {
        return compressedText;
    }

    @Override
    public String decode(String input, List<String> code) {
        if (code.isEmpty()) return input;

        Map<String, String> binaryToWord = createBinaryMap(code).entrySet()
            .stream()
            .collect(HashMap::new, (map, entry) -> map.put(entry.getValue(), entry.getKey()), HashMap::putAll);

        StringBuilder decodedText = new StringBuilder();
        String[] parts = input.split("\\s+");

        for (String part : parts) {
            String cleanedPart = part.replaceAll("[.,:;\"!()]", "");
            String decodedWord = binaryToWord.getOrDefault(cleanedPart, cleanedPart);
            decodedText.append(part.replace(cleanedPart, decodedWord)).append(" ");
        }

        return decodedText.toString().trim();
    }
}

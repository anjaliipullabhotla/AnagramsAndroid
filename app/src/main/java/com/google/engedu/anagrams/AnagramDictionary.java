/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.anagrams;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private Random random = new Random();
    private ArrayList<String> wordList;
    private HashSet<String> wordSet;
    private HashMap<String, ArrayList<String>> lettersToWord;
    private HashMap<Integer, ArrayList<String>> sizeToWords;
    private int wordLength = DEFAULT_WORD_LENGTH;

    public AnagramDictionary(Reader reader) throws IOException {
        
        BufferedReader in = new BufferedReader(reader);
        wordList = new ArrayList<>();
        wordSet = new HashSet<>();
        lettersToWord = new HashMap<>();
        sizeToWords= new HashMap<>();
        String line;
        
        while((line = in.readLine()) != null) {
            
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String sortedWord = sortLetters(word);
            int length = sortedWord.length();
            
            if (lettersToWord.containsKey(sortedWord)) {
                lettersToWord.get(sortedWord).add(word);
            } else {
                ArrayList<String> wordAsArrayList = new ArrayList<>();
                wordAsArrayList.add(word);
                lettersToWord.put(sortedWord, wordAsArrayList);
            }
            
            if (sizeToWords.containsKey(length)) {
                sizeToWords.get(length).add(word);
            } else {
                ArrayList<String> wordAsArrayList = new ArrayList<>();
                wordAsArrayList.add(word);
                sizeToWords.put(length, wordAsArrayList);
            }
        }
    }

    public boolean isGoodWord(String word, String base) {
        return wordSet.contains(word) && (!(word.contains(base)));
    }

    public String sortLetters(String word) {
        char charArray[] = word.toCharArray();
        Arrays.sort(charArray);
        return new String(charArray);
    }

    public ArrayList<String> getAnagrams(String targetWord) {
        String sortedTargetWord = sortLetters(targetWord);
        ArrayList<String> result = new ArrayList<>();
        for (int i = 0; i < wordList.size(); i++) {
            String word = wordList.get(i);
            if (sortedTargetWord.equals(sortLetters(word))) {
                result.add(word);
            }
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<>();
        for (char c = 'a'; c <= 'z'; c++) {
            ArrayList<String> resultPerCharacter = lettersToWord.get(sortLetters(word.concat("" + c)));
            if (lettersToWord.containsKey(sortLetters(word.concat("" + c)))) {
                ArrayList<String> resultWithoutSubstring = new ArrayList<>();
                for (int i = 0; i < resultPerCharacter.size(); i++) {
                    if (!(resultPerCharacter.get(i).contains(word))) {
                        resultWithoutSubstring.add(resultPerCharacter.get(i));
                    }
                }
                result.addAll(resultWithoutSubstring);
            }
        }
        return result;
    }

    public String pickGoodStarterWord() {
        ArrayList<String> wordOfSize = sizeToWords.get(wordLength);
        if (wordLength < MAX_WORD_LENGTH) { wordLength++; }
        while (true) {
            String word = wordOfSize.get(random.nextInt(wordOfSize.size()));
            if (getAnagramsWithOneMoreLetter(word).size() >= MIN_NUM_ANAGRAMS) { return word; }
        }
    }

}

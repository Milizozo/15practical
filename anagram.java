import java.io.*;
import java.util.*;

public class anagram{


public static String signature(String word){

    char[] letters = word.toCharArray();
    Arrays.sort(letters);
    return new String(letters);
}
public static void main(String[] args){

    String inputFile = "joyce1922_ulysses.text";
    File file = new File(inputFile);

    if(!file.exists()){
        System.out.println("Error: The file is not found" +inputFile);
        return;
    }

    Map<String, Integer>wordCount = new TreeMap<>();
    Map<String,ArrayList>anagramGroups = new TreeMap<>();

    try(BufferedReader reader =new BufferedReader(new FileReader(file))){
        String line;
        while((line=reader.readLine()) != null){
            String[] words = line.split("\\s+");

            for(String w:words){
                String clean = w.replaceAll("^[^a-zA-Z']+", "");
                clean = clean.replaceAll("[^a-zA-Z']+$", "");
                clean = clean.toLowerCase();

            if(clean.length() == 0)
            continue;

            wordCount.put(clean, wordCount.getOrDefault(clean,0) +1);
            }
        } 
    }
        catch(Exception e){
            e.printStackTrace();
        }
        for(String word : wordCount.keySet()){
            String sig =signature(word);
            if(!anagramGroups.containsKey(sig))
            anagramGroups.put(sig,new ArrayList<>());

            anagramGroups.get(sig).add(word);

        }
        List<String>lines = new ArrayList<>();

        for(ArrayList<String>list : anagramGroups.values()){
            if(list.size() >1){
                Collections.sort(list);
                String lineOut = String.join(" ",list);
                lines.add(lineOut +"\\\\");
                
                String rotated = lineOut;
                for(int i = 0; i<list.size()-1;i++){
                    int space = rotated.indexOf("");
                    rotated = rotated.substring(space+1) + " " + rotated.substring(0,space);
                    lines.add(rotated + "\\\\");

                }            
            }
        }
        File dir = new File("latex");
        if(!dir.exists())
        dir.mkdir();

        try(PrintWriter tex = new PrintWriter(new FileWriter("latex/theAnagrams.tex"))){
            char currentLetter = 'X';
            for(String lemma:lines){
                char intitial = Character.toUpperCase(lemma.charAt(0));
                if(initial != currentLetter){
                    currentLetter = initial;
                    tex.println();
                    tex.println("\\vspace{14pt}");
                    tex.println("\\noindent\\textbf{\\Large" +initial + "}\\\\*[+12pt]");
                }
                tex.println(lemma);
            }
            System.out.println("Anagram dictionary created.");
            System.out.println("Output file:latex/Anagrams.tex");
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

  }

  
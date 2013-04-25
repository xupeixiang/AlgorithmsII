/************************************************************
 * 
 * Copyright (c) 2013, Peixiang Xu(peixiangxu@gmail.com)
 * 
 * This program is free software: you can redistribute it 
 * and/or modify it under the terms of the GNU General Public 
 * License as published by the Free Software Foundation.
 * 
 ************************************************************/

import java.util.ArrayList;
import java.util.HashMap;


public class WordNet {
    
    // digraph for synsets
    private Digraph hypernymsGraph = null;
    
    private SAP sap = null;
    
    // synset id and its nouns
    private HashMap<Integer,String> synsetsData = new HashMap<Integer,String>();
    
    // noun and its synset ids 
    private HashMap<String,ArrayList<Integer>> nouns = new HashMap<String,ArrayList<Integer>>(); 
    
    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms)
    {
        In synsetsFileIn = new In(synsets);
        String syssetsLine = synsetsFileIn.readLine();
        while(syssetsLine != null)
        {
            String[] tokenStrings = syssetsLine.split(",");
            //if(tokenStrings.length != 3)
            //    throw new IllegalArgumentException();
            Integer id = Integer.parseInt(tokenStrings[0]);
            String[] nounStrings  = tokenStrings[1].split(" ");
            synsetsData.put(id, tokenStrings[1]);
            for(String noun:nounStrings)
            {
                // if not contained
                if(!nouns.containsKey(noun)){
                    nouns.put(noun,new ArrayList<Integer>());
                }
                nouns.get(noun).add(id);
            }
            syssetsLine = synsetsFileIn.readLine();
        }
        
        hypernymsGraph = new Digraph(synsetsData.size());
        int outDegreeNotZero = 0;
        In hypernymsFileIn = new In(hypernyms);
        String hypernymsLine = hypernymsFileIn.readLine();
        while(hypernymsLine != null)
        {
            String[] tokenIdStrings = hypernymsLine.split(",");
            Integer id = Integer.parseInt(tokenIdStrings[0]);
            outDegreeNotZero += 1;
            for(int i = 1;i < tokenIdStrings.length;i++)
            {
                Integer hypernymId = Integer.parseInt(tokenIdStrings[i]);
                hypernymsGraph.addEdge(id, hypernymId);
            }
            hypernymsLine = hypernymsFileIn.readLine();
        }
        
        
        this.sap = new SAP(hypernymsGraph);
        DirectedCycle dc = new DirectedCycle(hypernymsGraph);
        // more than two roots or cyclic
        if(outDegreeNotZero < synsetsData.size() - 1 ||dc.hasCycle()){
            throw new IllegalArgumentException();
        }
    }

    // returns all WordNet nouns
    public Iterable<String> nouns()
    {
        return this.nouns.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word)
    {
        return nouns.containsKey(word);
    }

    // distance between nounA and nounB 
    public int distance(String nounA, String nounB)
    {
        if(!nouns.containsKey(nounA) || !nouns.containsKey(nounB)){
            throw new IllegalArgumentException();
        }
        return sap.length(nouns.get(nounA), nouns.get(nounB));
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path 
    public String sap(String nounA, String nounB)
    {
        if(!nouns.containsKey(nounA) || !nouns.containsKey(nounB)){
            throw new IllegalArgumentException();
        }
        int ancestorId =  sap.ancestor(nouns.get(nounA), nouns.get(nounB));
        return synsetsData.get(ancestorId);
        
    }

    // for unit testing of this class
    public static void main(String[] args){
        
    }
}

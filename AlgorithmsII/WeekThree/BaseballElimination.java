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
import java.util.HashSet;


public class BaseballElimination {
    
    private HashMap<String, Integer> name2ID;
    private String[] id2Name;
    private int[] wins;
    private int[] loss;
    private int[] lefts;
    private int[][] games;
    // for trivial elimination
    private int maxWins;
    private String maxWinsTeam;
    private Iterable<String> eliminations;
    
    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename){
        In fileIn = new In(filename);
        int teamNum = Integer.parseInt(fileIn.readLine());
        // construct
        name2ID = new HashMap<String,Integer>(teamNum);
        id2Name = new String[teamNum];
        wins = new int[teamNum];
        maxWins = 0;
        maxWinsTeam = null;
        loss = new int[teamNum];
        lefts = new int[teamNum];
        games = new int[teamNum][teamNum];
        eliminations = null;
        
        // initialize
        for(int i = 0; i < teamNum; i++){
            String[] teamInfos = fileIn.readLine().trim().split(" +");
            name2ID.put(teamInfos[0], i);
            id2Name[i] = teamInfos[0];
            wins[i] = Integer.parseInt(teamInfos[1]);
            if(wins[i] > maxWins){
                maxWins = wins[i];
                maxWinsTeam = teamInfos[0];
            }
            loss[i] = Integer.parseInt(teamInfos[2]);
            lefts[i] = Integer.parseInt(teamInfos[3]);
            for(int j = 0; j < teamNum; j++){
                games[i][j] = Integer.parseInt(teamInfos[4 + j]);
            }
        }
        
    }
    
    // number of teams
    public int numberOfTeams(){
        return name2ID.size();
    }
    
    // all teams
    public Iterable<String> teams(){
        return name2ID.keySet();
    }
    
    // number of wins for given team
    public int wins(String team){
        if(!name2ID.containsKey(team)){
            throw new IllegalArgumentException();
        }
        return wins[name2ID.get(team)];
    }
    
    // number of losses for given team
    public int losses(String team){
        if(!name2ID.containsKey(team)){
            throw new IllegalArgumentException();
        }
        return loss[name2ID.get(team)];
    }
    
    // number of remaining games for given team
    public int remaining(String team){
        if(!name2ID.containsKey(team)){
            throw new IllegalArgumentException();
        }
        return lefts[name2ID.get(team)];
    }
    
    // number of remaining games between team1 and team2
    public int against(String team1, String team2){
        if(!name2ID.containsKey(team1) || !name2ID.containsKey(team2)){
            throw new IllegalArgumentException();
        }
        return games[name2ID.get(team1)][name2ID.get(team2)];
    }
    
    // is given team eliminated?
    public boolean isEliminated(String team){
        if(!name2ID.containsKey(team)){
            throw new IllegalArgumentException();
        }
        //current id of team
        int id = name2ID.get(team);
        
        //trivial elimination 
        if(wins[id] + lefts[id] < maxWins){
            ArrayList<String> eliminationsList = new ArrayList<String>();
            eliminationsList.add(maxWinsTeam);
            eliminations = eliminationsList;
            return true;
        }
        //nontrivial elimination 
        // # of vertices = 2 + n-1 + C(n-1,2)
        FlowNetwork baseball = new FlowNetwork(2 + numberOfTeams() - 1 
                + (numberOfTeams() - 1) * (numberOfTeams() - 2)/2);
        
        // max total wins for each team
        int maxIdWins = wins[id] + lefts[id];
        
        // add edge to t( id = 1)
        for(int i = 0; i < numberOfTeams(); i++){
            if(i != id){
                baseball.addEdge(new FlowEdge(teamId2EdgeId(i, id), 1, maxIdWins - wins[i]));
            }
        }
        
        // number of edges already added
        int numOfEdges = 2 + numberOfTeams() - 1;
        
        // add edges from s( id = 0) and to 0~n-1
        for(int j = 0; j < numberOfTeams(); j++){
            if(j != id){
                for(int k = j + 1;k < numberOfTeams(); k++){
                    if(k != id){
                        baseball.addEdge(new FlowEdge(0,numOfEdges,games[j][k]));
                        baseball.addEdge(new FlowEdge(numOfEdges, teamId2EdgeId(j, id), games[j][k]));
                        baseball.addEdge(new FlowEdge(numOfEdges, teamId2EdgeId(k, id), games[j][k]));
                        numOfEdges++;
                    }
                }
            }
        }
        
        //Max flow
        FordFulkerson baseballFordFulkerson = new FordFulkerson(baseball, 0, 1);
        for(FlowEdge e:baseball.adj(0)){
            if(e.flow() != e.capacity()){
                findSubsets(baseball,baseballFordFulkerson,id);
                return true;
            }
        }
        eliminations = null;
        return false;
    }
    
    private int teamId2EdgeId(int teamId,int desTeamId){
        return teamId < desTeamId?teamId + 2:teamId + 2 - 1;
    }
    
    private int edgeId2TeamId(int edgeId,int desTeamId){
        return edgeId - 2 < desTeamId?edgeId - 2:edgeId - 2 + 1;
    }
    
    private void findSubsets(FlowNetwork fn,FordFulkerson ff,int desTeamId){
        HashSet<Integer> ids = new HashSet<Integer>();
        for(FlowEdge e:fn.adj(0)){
            int matchId = e.other(0);
            // belongs to the s side
            if(ff.inCut(matchId)){
                for(FlowEdge adj:fn.adj(matchId)){
                    int edgeId = adj.other(matchId);
                    if(edgeId != 0){
                        ids.add(edgeId2TeamId(edgeId,desTeamId));
                    }
                }
            }
        }
        ArrayList<String> eliminationsList = new ArrayList<String>();
        for(Integer id:ids){
            eliminationsList.add(id2Name[id]);
        }
        eliminations = eliminationsList;
    }
    
    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team){
        if(!name2ID.containsKey(team)){
            throw new IllegalArgumentException();
        }
        if(isEliminated(team)){
            return eliminations;
        }
        else {
            return null;
        }
    }
    
    // test client
    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team))
                    StdOut.print(t + " ");
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

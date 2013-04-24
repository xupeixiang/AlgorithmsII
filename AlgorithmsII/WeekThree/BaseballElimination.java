import java.util.HashMap;


public class BaseballElimination {
	
	private HashMap<String, Integer> name2ID;
	private int[] wins;
	private int[] loss;
	private int[] lefts;
	private int[][] games;
	
	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename){
		In fileIn = new In(filename);
		int teamNum = Integer.parseInt(fileIn.readLine());
		// construct
		name2ID = new HashMap<String,Integer>(teamNum);
		wins = new int[teamNum];
		loss = new int[teamNum];
		lefts = new int[teamNum];
		games = new int[teamNum][teamNum];
		
		// initialize
		for(int i = 0; i < teamNum; i++){
			String[] teamInfos = fileIn.readLine().split(" +");
			name2ID.put(teamInfos[0], i);
			wins[i] = Integer.parseInt(teamInfos[1]);
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
		return true;
	}
	
	 // subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team){
		if(!name2ID.containsKey(team)){
			throw new IllegalArgumentException();
		}
		return teams();
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


import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;
import java.util.ArrayList;

public class BaseballElimination {

    private int V;//vertex num
    private int matchesNum;
    private int thisFlow;
    private int[][] g;
    private ST<String,Team> set;//team name to team id
    private ST<Integer,Integer> v2id;//vertex id to team id
    private String[] teamList;
    private int teamNum;
    private class Team{
        private final int id;
        private final int win;
        private final int loss;
        private final int left;

        private Team(int id,int win,int loss,int left){
            this.id = id;
            this.win = win;
            this.loss = loss;
            this.left = left;
        }
    }

    private void validdateTeam(String name){
        if(name==null||!this.set.contains(name)){
            throw new IllegalArgumentException();
        }
    }

    public BaseballElimination(String filename){

        if(filename==null){
            throw new IllegalArgumentException();
        }

        In in = new In(filename);

        this.teamNum = Integer.parseInt(in.readLine());
        this.g = new int[teamNum][teamNum];
        this.set = new ST<String,Team>();
        this.v2id = new ST<Integer, Integer>();
        this.teamList = new String[teamNum];

        int id = 0;
        while(in.hasNextLine()){

            String line = in.readLine().trim();
            //正则表达式 " +" 表示多个空格
            String [] info = line.split(" +");

            String name = info[0];

            int win = Integer.parseInt(info[1]);
            int loss = Integer.parseInt(info[2]);
            int left = Integer.parseInt(info[3]);

            if(!this.set.contains(name)){
                teamList[id] = name;
                set.put(name,new Team(id,win,loss,left));
                for(int i=0;i<teamNum;i++){
                    g[id][i] = Integer.parseInt(info[i+4]);
                }
                id++;
            }

        }
    }                                                               // create a baseball division from given filename in format specified below
    public              int numberOfTeams(){
        return this.teamNum;
    }                        // number of teams
    public Iterable<String> teams(){
        return this.set.keys();
    }// all teams
    public              int wins(String team){
        validdateTeam(team);
        return this.set.get(team).win;
    }                      // number of wins for given team
    public              int losses(String team){
        validdateTeam(team);
        return this.set.get(team).loss;
    }                    // number of losses for given team
    public              int remaining(String team){
        validdateTeam(team);
        return this.set.get(team).left;
    }                 // number of remaining games for given team
    public              int against(String team1, String team2){
        validdateTeam(team1);
        validdateTeam(team2);
        int id1 = this.set.get(team1).id;
        int id2 = this.set.get(team2).id;
        return this.g[id1][id2];
    }    // number of remaining games between team1 and team2

    private FlowNetwork constructNetWork(String team){

        validdateTeam(team);

        Team info = this.set.get(team);
        int testID = info.id; //tested team id
        int mostWin = info.win+info.left; //team team most win counts
        thisFlow = 0;

        this.matchesNum = ((teamNum-1)*(teamNum-2))/2;
        this.V = 2+(teamNum-1)+this.matchesNum;

        int s = 0; //souce node index
        int t = V-1; //target node index
        int matchIndex= 1;
        //matchesNum+1~matchesNum+teamNum are team indexes
        int IndexI = matchesNum; //team index
        int IndexJ = IndexI;//team index

        FlowNetwork network = new FlowNetwork(V);

        for(int i=0;i<teamNum;i++){

            if(i==testID)
                continue;

            IndexI++;
            IndexJ = IndexI;

            //case 1
            if(mostWin<wins(teamList[i])){
                return null;
            }

            for(int j=i+1;j<teamNum;j++){

                if(j==testID)
                    continue;
                IndexJ++;
                thisFlow+=g[i][j];
                network.addEdge(new FlowEdge(s,matchIndex,g[i][j]));
                network.addEdge(new FlowEdge(matchIndex,IndexI,Double.POSITIVE_INFINITY));
                network.addEdge(new FlowEdge(matchIndex,IndexJ,Double.POSITIVE_INFINITY));
                matchIndex++;
            }
            v2id.put(IndexI,i);
            network.addEdge(new FlowEdge(IndexI,t,mostWin-wins(teamList[i])));
        }

        return network;
    }

    public boolean isEliminated(String team){

        validdateTeam(team);
        FlowNetwork network = constructNetWork(team);
        if(network==null)
            return true;
        else{
            FordFulkerson ff = new FordFulkerson(network,0,V-1);
            return thisFlow>ff.value();
        }
    }

    // is given team eliminated?
    public Iterable<String> certificateOfElimination(String team){
        validdateTeam(team);

        if(!isEliminated(team)){
            return null;
        }else{
            Queue<String> q = new Queue<String>();
            int thisTeamID = this.set.get(team).id;
            FlowNetwork network = constructNetWork(team);
            if(network==null){
                int thisTeamMostWin = this.set.get(team).win+this.set.get(team).left;
                for(int i=0;i<teamNum;i++){
                    if(i==thisTeamID)
                        continue;
                    //如果待检查队伍RP爆发能赢得次数还是少于某支队伍已经赢了的次数，那么把那个“某支队伍”push到q里面
                    if(thisTeamMostWin<this.set.get(teamList[i]).win){
                        q.enqueue(teamList[i]);
                    }
                }

            }else{
                FordFulkerson ff = new FordFulkerson(network,0,V-1);
                //注意ff的incut函数的参数是vertex id
                for(int i=matchesNum+1;i<V;i++){
                    if(ff.inCut(i)){
                        q.enqueue(teamList[v2id.get(i)]);
                    }
                }
            }
            return q;
        }
    }
    // subset R of teams that eliminates given team; null if not eliminated

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        for (String team : division.teams()) {
            if (division.isEliminated(team)) {
                StdOut.print(team + " is eliminated by the subset R = { ");
                for (String t : division.certificateOfElimination(team)) {
                    StdOut.print(t + " ");
                }
                StdOut.println("}");
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}

import java.io.Serializable;
import java.util.*;

class BaccaratInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public String betOnWho;
    public double currentBet;
    public ArrayList<String> playerHand = new ArrayList<String>();
    public ArrayList<String> bankerHand = new ArrayList<String>();
    //int port;
    //String ipAddress;
    public Boolean quit; //exit game
    public double totalEarnings;
    public double currentEarnings;
    public String winner;
    public boolean playerDrew;
    public boolean bankerDrew;
    public int naturalWin;
    public int pPoints;
    public int bPoints;
    public ArrayList<String> pCardVals = new ArrayList<String>();
    public ArrayList<String> bCardVals = new ArrayList<String>();
}
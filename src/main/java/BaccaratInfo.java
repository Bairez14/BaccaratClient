import java.io.Serializable;
import java.util.*;

class BaccaratInfo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    String betOnWho;
    double currentBet;
    ArrayList<String> playerHand = new ArrayList<String>();
    ArrayList<String> bankerHand = new ArrayList<String>();
    //int port;
    //String ipAddress;
    Boolean quit; //exit game
    double totalEarnings;
    double currentEarnings;
    String winner;
    boolean playerDrew;
    boolean bankerDrew;
    int naturalWin;
    
}
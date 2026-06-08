package model;

import boardifier.model.*;

import java.util.ArrayList;

/**
 * TablutStageModel defines the model for the single stage in "The Tablut". Indeed,
 * there are no levels in this game: a party starts and when it's done, the game is also done.
 *
 * TablutStageModel must define all that is needed to manage a party : state variables and game elements.
 * In the present case, there are only 2 state variables that represent the number of pawns to play by each player.
 * It is used to detect the end of the party.
 * For game elements, it depends on what is chosen as a final UI design. For that demo, there are 12 elements used
 * to represent the state : the main board, 2 pots, 8 pawns, and a text for current player.
 *
 * WARNING ! TablutStageModel DOES NOT create itself the game elements because it would prevent the possibility to mock
 * game element classes for unit testing purposes. This is why TablutStageModel just defines the game elements and the methods
 * to set this elements.
 * The instanciation of the elements is done by the TablutStageFactory, which uses the provided setters.
 *
 * TablutStageModel must also contain methods to check/modify the game state when given events occur. This is the role of
 * setupCallbacks() method that defines a callback function that must be called when a pawn is put in a container.
 * This is done by calling onPutInContainer() method, with the callback function as a parameter. After that call, boardifier
 * will be able to call the callback function automatically when a pawn is put in a container.
 * NB1: callback functions MUST BE defined with a lambda expression (i.e. an arrow function).
 * NB2:  there are other methods to defines callbacks for other events (see onXXX methods in GameStageModel)
 * In "The Tablut", everytime a pawn is put in the main board, we have to check if the party is ended and in this case, who is the winner.
 * This is the role of computePartyResult(), which is called by the callback function if there is no more pawn to play.
 *
 */
public class TablutStageModel extends GameStageModel {

    // states
    public static final int STATE_SELECT_PAWN = 1;
    public static final int STATE_SELECT_DEST = 2;

    // define stage state variables
    private WinMode winMode;
    private KingMoveMode kingMoveMode;
    // 0 for no AI, 1 for one AI, 2 for two AIs
    private int mode;
    private ArrayList<AIMode> aiModesUsed;

    // define stage game elements
    private TablutBoard board;
    private Pawn[] pawns;
    private ArrayList<Fortress> fortresses;
    private TextElement playerName;
    // Uncomment next line if the example with a main container is used. see end of TablutStageFactory and TablutStageView
    //private ContainerElement mainContainer;

    public TablutStageModel(String name, Model model) {
        super(name, model);
        this.kingMoveMode = KingMoveMode.LIMITLESS;
        this.winMode = WinMode.BORDERS;
        this.aiModesUsed = new ArrayList<>();

        setAIModes(); // defaults to minmax AI, can be changed later in options

        setState(STATE_SELECT_PAWN);
        setupCallbacks();
    }

    public void setAIModes() {
        int cnt = 0;
        for (Player player : this.model.getPlayers()) {
            if (player.getName().contains("computer")) {
                aiModesUsed.add(AIMode.SIMPLE_EVAL);
                cnt++;
            }
        }
        this.mode = cnt;
    }

    public TablutBoard getBoard() {
        return board;
    }

    public Pawn[] getPawns() {
        return pawns;
    }

    public ArrayList<Fortress> getFortresses() {
        return fortresses;
    }

    public TextElement getPlayerName() {
        return playerName;
    }

    public void setBoard(TablutBoard tb) {
        board = tb;
        addContainer(board);
    }

    public void setPawns(Pawn[] p) {
        pawns = p;
        for (Pawn pawn : pawns) {
            board.addElement(pawn, (int)pawn.getX(), (int)pawn.getY());
            addElement(pawn);
        }
    }

    public void setFortresses(ArrayList<Fortress> fortressList) {
        this.fortresses = fortressList;
        for (Fortress fortress : fortressList) {
            board.addElement(fortress, (int)fortress.getX(), (int)fortress.getY());
            addElement(fortress);
        }
    }

    public void setPlayerName(TextElement pn) {
        playerName = pn;
        addElement(playerName);
    }

    private void setupCallbacks() {
        onMoveInContainer( (element, containerDest, rowDest, colDest) -> {
            if (containerDest != board) return;


            // TODO : do something to check for raichi and tuichi
            
            if (board.areSwedishWinning()) {
                showGameResult(true);
            }

        });

        onRemoveFromContainer( (element, container, row, col) -> {
            // Checks if the team that just played put the opposite team in stalemate
            Team pawnTeam = ((Pawn) element).getTeam();
            if (board.isStalemate(pawnTeam)) {
                System.out.println(pawnTeam + " was put in a stalemate");
                showGameResult(pawnTeam.oppositeTeam() == Team.SWEDISH);
//            } else {
//              // print to debug
//
//                System.out.println(pawnTeam.oppositeTeam() + " didn't put " + pawnTeam + " into a stalemate");
//                System.out.println(pawnTeam + " can still play :");
//
//                List<Move> moveList = board.computeAllValidCells(pawnTeam);
//
//                for (Move move : moveList) {
//                    System.out.println("From x:" + move.sourceX + " y:" + move.sourceY + " to x:" + move.destX + " y:" + move.destY);
//                }
            }

            if (getBoard().areMuscovitesWinning()) showGameResult(false);
        });
    }

    private void showGameResult(boolean swedishWon) {
        int idWinner = -1;
        //String winnerString;

        if (swedishWon) {
            idWinner = 0;
        } else {
            idWinner = 1;
        }
        /*model.setIdPlayer(idWinner);
        winnerString = model.getCurrentPlayerName();

        System.out.println(winnerString + " won !");*/
        // set the winner
        model.setIdWinner(idWinner);
        // stop de the game
        model.stopStage();
    }

    @Override
    public StageElementsFactory getDefaultElementFactory() {
        return new TablutStageFactory(this);
    }
 
    public boolean isFortressAt(int x, int y) {
        for (Fortress fortress : fortresses) {
            if (fortress.getX() == x && fortress.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public WinMode getWinMode() {
        return winMode;
    }

    public KingMoveMode getKingMoveMode() {
        return kingMoveMode;
    }

    public void setWinMode(WinMode winMode) {
        this.winMode = winMode;

        this.board.setWinCoords(this.winMode.winCoords(this.board));
    }

    public void setKingMoveMode(KingMoveMode kingMoveMode) {
        this.kingMoveMode = kingMoveMode;
    }

    public ArrayList<AIMode> getAIModesUsed() {
        return aiModesUsed;
    }

    public void setAIModesUsed(ArrayList<AIMode> aiModesUsed) {
        this.aiModesUsed = aiModesUsed;
    }

    public void setAIModesUsed(AIMode AI) {
        ArrayList<AIMode> aiModes = new ArrayList<>();
        aiModes.add(AI);

        this.aiModesUsed = aiModes;
    }

    public void setAIModesUsed(AIMode SwedishAI, AIMode MuscoAI) {
        ArrayList<AIMode> aiModes = new ArrayList<>();
        aiModes.add(SwedishAI);
        aiModes.add(MuscoAI);

        this.aiModesUsed = aiModes;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }
}

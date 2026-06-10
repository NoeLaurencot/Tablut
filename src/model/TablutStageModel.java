package model;

import boardifier.model.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    // 0 for no AI, 1 for one AI, 2 for two AIs. Used in getDeciderType
    private int mode;
    private ArrayList<AIMode> aiModesUsed;

    // define stage game elements
    private TablutBoard board;
    private Pawn[] pawns;
    private ArrayList<Fortress> fortresses;
    private TextElement playerName;
    private RaichiTuichiMessage message;
    private MoveHistory moveHistory;
    // Uncomment next line if the example with a main container is used. see end of TablutStageFactory and TablutStageView
    private TablutOptionModel tablutOptionModel;

    public TablutStageModel(String name, Model model) {
        super(name, model);
        this.kingMoveMode = KingMoveMode.LIMITLESS;
        this.winMode = WinMode.BORDERS;
        this.aiModesUsed = new ArrayList<>();

        setState(STATE_SELECT_PAWN);
        setupCallbacks();
    }

    public void setAIModes() {
        this.aiModesUsed = TablutOptionModel.getAiMode();
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

    public RaichiTuichiMessage getMessage() {
        return message;
    }

    public MoveHistory getMoveHistory() {
        return moveHistory;
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

    public void setMessage(RaichiTuichiMessage m) {
        message = m;
        addElement(m);
    }

    public void setMoveHistory(MoveHistory mh) {
        moveHistory = mh;
        addElement(moveHistory);
    }

    public void setEmptyMovesHistory(List<MoveElement> emptyMoves) {
        for (MoveElement moveElement : emptyMoves) {
            moveHistory.addEmptyMove(moveElement);
            addElement(moveElement);
        }

        moveHistory.resetInsertMove();
    }


    private void setupCallbacks() {
        onMoveInContainer( (element, containerDest, rowDest, colDest) -> {
            if (containerDest != board) return;

            playSound("move.wav");

            if (element instanceof King) {
                int cnt = 0;
                boolean done = false;
                for (Coordinate winCoord : board.getWinCoords()) {
                    if (winCoord.getX() == rowDest && winCoord.getY() == colDest) {
                        done = true;
                        break;
                    }
                    if (board.canSeeCoordinate(board.getKingPosition(), winCoord) && board.isEmptyAt(winCoord.getX(), winCoord.getY()))
                        cnt++;
                }

                if (cnt == 1 && !done) {
                    message.setText("Raichi !");
                } else if (cnt >= 2 && !done) {
                    message.setText("Tuichi !");
                } else {
                    message.setText("");
                }
            } else {
                updateRaichiMessage();
            }

            if (board.areSwedishWinning()) {
                playSound("victory.wav");
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

            if (getBoard().areMuscovitesWinning()) {

                playSound("explosion.wav");
                showGameResult(false);
            }
        });
    }

    private void updateRaichiMessage() {
        Coordinate kingPos = board.getKingPosition();

        int cnt = 0;
        boolean done = false;
        for (Coordinate winCoord : board.getWinCoords()) {
            if (kingPos == winCoord) {
                done = true;
                break;
            }
            if (board.canSeeCoordinate(kingPos, winCoord) && board.isEmptyAt(winCoord.getX(), winCoord.getY()))
                cnt++;
        }

        if (cnt == 0 || done) {
            message.setText("");
        }
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
        model.stopGame();
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

    public void addMoveInHistory(Move move) {
        moveHistory.addMove(move);
    }

    public void addMoveInHistory(MoveElement moveElement) {
        moveHistory.addMove(moveElement);
    }

    private void playSound(String fileName) {
        try {
            File soundFile = new File(getClass().getResource("/" + fileName).toURI());

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            clip.start();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

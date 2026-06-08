package model;

import boardifier.model.Model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

public class TablutStageFactoryUnitTest {

    private TablutStageModel stageModel;
    private TablutStageFactory factory;
    private Model mockModel;

    @BeforeEach
    public void setup() {
        // Mock the root Model if TablutStageModel requires it internally
        mockModel = Mockito.mock(Model.class);

        // Initialize your stage model
        stageModel = new TablutStageModel("stage", mockModel);

        // Initialize the factory under test
        factory = new TablutStageFactory(stageModel);
    }

    @Test
    public void testSetupPawns_CreatesCorrectTotalAndDistribution() {
        factory.setup();

        Pawn[] pawns = stageModel.getPawns();

        assertNotNull(pawns, "Pawns array not null after setup");
        assertEquals(25, pawns.length, "25 pawns in Tablut");

        assertInstanceOf(King.class, pawns[24], "index 24 must be the King");
        assertEquals(4, pawns[24].getX(), "King at X coordinate 4");
        assertEquals(4, pawns[24].getY(), "King at Y coordinate 4");

        assertInstanceOf(Soldier.class, pawns[0], "Pawn at index 0 is Soldier");
        assertEquals(Team.SWEDISH, pawns[0].getTeam(), "Pawn 0 is SWEDISH team");
        assertEquals(4, pawns[0].getX());
        assertEquals(2, pawns[0].getY());

        assertEquals(Team.MUSCOVITES, pawns[8].getTeam(), "Pawn 8 is MUSCOVITES team");
    }

    @Test
    public void testSetupPawns_CountsTeamsAccurately() {
        factory.setup();
        Pawn[] pawns = stageModel.getPawns();

        int swedishCount = 0;
        int muscoviteCount = 0;
        int kingCount = 0;

        for (Pawn pawn : pawns) {
            if (pawn instanceof King) {
                kingCount++;
            } else if (pawn.getTeam() == Team.SWEDISH) {
                swedishCount++;
            } else if (pawn.getTeam() == Team.MUSCOVITES) {
                muscoviteCount++;
            }
        }

        assertEquals(1, kingCount, "1 King");
        assertEquals(8, swedishCount, "8 Swedish soldiers");
        assertEquals(16, muscoviteCount, "16 Muscovite soldiers");
    }

    @Test
    public void testSetupFortresses() {
        factory.setup();

        Fortress[] fortresses = stageModel.getFortresses();

        assertNotNull(fortresses, "Fortresses not null");
        assertEquals(5, fortresses.length, "5 fortresses should be created");

        assertEquals(0, fortresses[0].getX(), "first fortress at X 0");
        assertEquals(0, fortresses[0].getY(), "first fortress at Y 0");

        assertEquals(4, fortresses[4].getX(), "first fortress at X 4");
        assertEquals(4, fortresses[4].getY(), "first fortress at Y 4");
    }
}
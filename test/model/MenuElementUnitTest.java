package model;

import boardifier.model.GameStageModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class MenuElementUnitTest {

    private GameStageModel mockStageModel;
    private MenuElement menuElement;

    @BeforeEach
    public void setup() {
        mockStageModel = Mockito.mock(GameStageModel.class);
        menuElement = new MenuElement(mockStageModel, "");
    }

    @Test
    public void testConstructor_InitializesEmptyState() {
        assertNotNull(menuElement.getMenuEntries());
        assertTrue(menuElement.getMenuEntries().isEmpty());
    }

    @Test
    public void testSetMenuEntries() {
        ArrayList<MenuEntry> list = new ArrayList<>();
        MenuEntry entry = new MenuEntry(0, "Option 1", new ArrayList<>(), 1);
        list.add(entry);

        menuElement.setMenuEntries(list);

        assertEquals(1, menuElement.getMenuEntries().size());
        assertEquals(entry, menuElement.getMenuEntries().get(0));
    }

    @Test
    public void testAddEntry() {
        MenuEntry entry = new MenuEntry(0, "Option 1", new ArrayList<>(), 1);
        menuElement.addEntry(entry);

        assertEquals(1, menuElement.getMenuEntries().size());
        assertTrue(menuElement.getMenuEntries().contains(entry));
    }

    @Test
    public void testClearEntries() {
        menuElement.addEntry(new MenuEntry(0, "Option 1", new ArrayList<>(), 1));
        assertFalse(menuElement.getMenuEntries().isEmpty());

        menuElement.clearEntries();

        assertTrue(menuElement.getMenuEntries().isEmpty());
    }

    @Test
    public void testAddPrompt_WhenNull_SetsEmptyString() {
        menuElement.addPrompt(null);
        menuElement.setMenuString();
        assertEquals("", menuElement.getMenuString());
    }

    @Test
    public void testAddPrompt_WhenValidString_SetsPromptValue() {
        menuElement.addPrompt("Choose:");
        menuElement.setMenuString();
        assertEquals("\nChoose:", menuElement.getMenuString());
    }

    @Test
    public void testSetMenuString_WhenEntriesAndPromptAreEmpty() {
        menuElement.setMenuString();
        assertEquals("", menuElement.getMenuString());
    }

    @Test
    public void testSetMenuString_WhenHasEntriesButNoPrompt() {
        menuElement.addEntry(new MenuEntry(0, "Play", new ArrayList<>(), 1));

        menuElement.setMenuString();
        assertEquals("1 - Play\n", menuElement.getMenuString());
    }

    @Test
    public void testSetMenuString_WhenNoEntriesWithPrompt() {
        menuElement.addPrompt("Select an option: ");
        menuElement.setMenuString();
        assertEquals("\nSelect an option: ", menuElement.getMenuString());
    }

    @Test
    public void testSetMenuString_WhenEntriesDoNotEndInNewlineWithPrompt() {
        menuElement.addEntry(new MenuEntry(0, "Exit", new ArrayList<>(), 1));
        menuElement.addPrompt("Choose now:");

        menuElement.setMenuString();

        assertEquals("1 - Exit\n\nChoose now:", menuElement.getMenuString());
    }

    @Test
    public void testSetMenuString_WithMultipleEntries() {
        MenuEntry e1 = new MenuEntry(0, "Play", new ArrayList<>(), 1);
        MenuEntry e2 = new MenuEntry(0, "Exit", new ArrayList<>(), 2);

        menuElement.addEntry(e1);
        menuElement.addEntry(e2);

        menuElement.setMenuString();

        assertEquals("1 - Play\n2 - Exit\n", menuElement.getMenuString());
    }

}
package model;

import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;

public class MenuEntryUnitTest {

    @Test
    public void testConstructorAndGetters() {
        ArrayList<MenuEntry> submenu = new ArrayList<>();
        MenuEntry entry = new MenuEntry(4, "Test", submenu, 2);

        assertEquals(4, entry.getLevel());
        assertEquals("Test", entry.getName());
        assertSame(submenu, entry.getSubmenu());
        assertEquals(2, entry.getEntryQuantity());
        assertEquals(1, entry.getPrefixLevel()); // 4 % 3 = 1
    }

    @Test
    public void testSetters() {
        MenuEntry entry = new MenuEntry(0, "Old", new ArrayList<>(), 1);
        ArrayList<MenuEntry> newSubmenu = new ArrayList<>();

        entry.setName("New");
        entry.setLevel(3);
        entry.setEntryQuantity(5);
        entry.setPrefixLevel(2);
        entry.setSubmenu(newSubmenu);

        assertEquals("New", entry.getName());
        assertEquals(3, entry.getLevel());
        assertEquals(5, entry.getEntryQuantity());
        assertEquals(2, entry.getPrefixLevel());
        assertSame(newSubmenu, entry.getSubmenu());
    }

    @Test
    public void testAddSubmenuEntry() {
        MenuEntry parent = new MenuEntry(0, "Parent", new ArrayList<>(), 1);
        MenuEntry child = new MenuEntry(1, "Child", new ArrayList<>(), 1);

        parent.addSubmenuEntry(child);

        assertEquals(1, parent.getSubmenu().size());
        assertSame(child, parent.getSubmenu().get(0));
    }

    @Test
    public void testToString_PrefixLevel0_UsesNumericQuantity() {
        MenuEntry entry = new MenuEntry(0, "Root Option", new ArrayList<>(), 3);

        String expected = "3 - Root Option\n";
        assertEquals(expected, entry.toString());
    }

    @Test
    public void testToString_PrefixLevel1_UsesUpperCaseChar() {
        MenuEntry entry = new MenuEntry(1, "Sub Option", new ArrayList<>(), 2);

        String expected = "  B - Sub Option\n";
        assertEquals(expected, entry.toString());
    }

    @Test
    public void testToString_PrefixLevel2_UsesLowerCaseChar() {
        MenuEntry entry = new MenuEntry(2, "Sub Sub Option", new ArrayList<>(), 3);

        String expected = "    c - Sub Sub Option\n";
        assertEquals(expected, entry.toString());
    }

    @Test
    public void testToString_DefaultBranch_ForcedManually() {
        MenuEntry entry = new MenuEntry(0, "Forced Default", new ArrayList<>(), 7);
        entry.setPrefixLevel(99); // Force default branch execution

        String expected = "7 - Forced Default\n";
        assertEquals(expected, entry.toString());
    }

    @Test
    public void testToString_WithSubmenuTreeRecursion() {
        MenuEntry parent = new MenuEntry(0, "Main Menu", new ArrayList<>(), 1);
        MenuEntry child = new MenuEntry(1, "Sub Menu Item", new ArrayList<>(), 1);

        parent.addSubmenuEntry(child);

        StringBuilder expected = new StringBuilder();
        expected.append("1 - Main Menu\n");
        expected.append("  A - Sub Menu Item\n");

        assertEquals(expected.toString(), parent.toString());
    }
}
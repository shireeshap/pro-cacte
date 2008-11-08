package gov.nih.nci.ctcae.core.domain;

import junit.framework.TestCase;

import java.util.ArrayList;

public class RaceEnumTest extends TestCase {

    public void testrace() {
        Race race = Race.AMERICANINDIAN;
        assertEquals("American Indian or Alaska Native", race.toString());
        race = Race.ASIAN;
        assertEquals("Asian", race.toString());
        race = Race.BLACK;
        assertEquals("Black or African American", race.toString());
        race = Race.NATIVEHAWAII;
        assertEquals("Native Hawaiian or Other PacificIslander", race.toString());
        race = Race.NOTREPORTED;
        assertEquals("Not Reported", race.toString());
        race = Race.UNKNOWN;
        assertEquals("Unknown", race.toString());
        race = Race.WHITE;
        assertEquals("White", race.toString());
    }


    public void testGetAllraces() {
        ArrayList<Race> races = Race.getAllRaces();
        for (Race race : Race.values()) {
            assertTrue(races.contains(race));
        }
    }

}

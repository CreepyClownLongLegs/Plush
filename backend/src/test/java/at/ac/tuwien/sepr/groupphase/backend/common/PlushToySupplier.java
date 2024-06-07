package at.ac.tuwien.sepr.groupphase.backend.common;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_COLOR;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_DESCRIPTION;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_HP;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_NAME;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_PRICE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_SIZE;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_STRENGTH;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_TAX_CLASS;
import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.TEST_PLUSHTOY_WEIGHT;

import org.springframework.stereotype.Component;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;

@Component
public class PlushToySupplier {

    public PlushToy getPlushie() {
        PlushToy plushy = new PlushToy();
        plushy.setName(TEST_PLUSHTOY_NAME);
        plushy.setPrice(TEST_PLUSHTOY_PRICE);
        plushy.setTaxClass(TEST_PLUSHTOY_TAX_CLASS);
        plushy.setWeight(TEST_PLUSHTOY_WEIGHT);
        plushy.setColor(Color.valueOf(TEST_PLUSHTOY_COLOR));
        plushy.setSize(Size.valueOf(TEST_PLUSHTOY_SIZE));
        plushy.setHp(TEST_PLUSHTOY_HP);
        plushy.setStrength(TEST_PLUSHTOY_STRENGTH);
        plushy.setDescription(TEST_PLUSHTOY_DESCRIPTION);
        return plushy;
    }
}

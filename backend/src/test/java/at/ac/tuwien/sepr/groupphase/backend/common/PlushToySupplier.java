package at.ac.tuwien.sepr.groupphase.backend.common;

import at.ac.tuwien.sepr.groupphase.backend.entity.Color;
import at.ac.tuwien.sepr.groupphase.backend.entity.PlushToy;
import at.ac.tuwien.sepr.groupphase.backend.entity.Size;
import org.springframework.stereotype.Component;

import static at.ac.tuwien.sepr.groupphase.backend.basetest.PlushToyTestData.*;

@Component
public class PlushToySupplier  {

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
        return plushy;
    }
}


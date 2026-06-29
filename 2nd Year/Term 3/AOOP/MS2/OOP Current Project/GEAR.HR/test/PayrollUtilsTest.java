import static org.junit.Assert.assertEquals;

import org.junit.Test;
import util.PayrollUtils;

/**
 * Unit tests for the statutory payroll math in {@link PayrollUtils}
 * (SSS bracket lookup, PhilHealth, Pag-IBIG, and withholding tax).
 */
public class PayrollUtilsTest {

    private static final double DELTA = 0.001;

    @Test
    public void sssAmountUsesBracketBoundaries() {
        assertEquals(250.0, PayrollUtils.calculateSSSAmount(0), DELTA);
        assertEquals(250.0, PayrollUtils.calculateSSSAmount(5249.99), DELTA);
        assertEquals(275.0, PayrollUtils.calculateSSSAmount(5250), DELTA);
        assertEquals(1750.0, PayrollUtils.calculateSSSAmount(34750), DELTA);
        assertEquals(1750.0, PayrollUtils.calculateSSSAmount(1_000_000), DELTA);
    }

    @Test
    public void philHealthIsFivePercentClampedBetween500And5000() {
        // 5% of 1000 = 50 -> floored to 500
        assertEquals(500.0, PayrollUtils.calculatePhilHealthAmount(1000), DELTA);
        // 5% of 20000 = 1000 -> within range
        assertEquals(1000.0, PayrollUtils.calculatePhilHealthAmount(20000), DELTA);
        // 5% of 200000 = 10000 -> capped to 5000
        assertEquals(5000.0, PayrollUtils.calculatePhilHealthAmount(200000), DELTA);
    }

    @Test
    public void pagIbigIsTwoPercentCappedAt200() {
        // 2% of 5000 = 100 -> within cap
        assertEquals(100.0, PayrollUtils.calculatePagIbigAmount(5000), DELTA);
        // 2% of 20000 = 400 -> capped to 200
        assertEquals(200.0, PayrollUtils.calculatePagIbigAmount(20000), DELTA);
    }

    @Test
    public void withholdingTaxIsZeroBelowThreshold() {
        // netTaxable = 20000 + 0 = 20000 <= 20833 -> no tax
        assertEquals(0.0, PayrollUtils.calculateWithholdingTax(20000, 0, 0, 0), DELTA);
    }

    @Test
    public void withholdingTaxUsesTwentyPercentBracket() {
        // netTaxable = 30000 -> (30000 - 20833) * 0.20 + 0 = 1833.4
        assertEquals(1833.4, PayrollUtils.calculateWithholdingTax(30000, 0, 0, 0), DELTA);
    }

    @Test
    public void withholdingTaxIncludesAllowancesInTaxableBase() {
        // netTaxable = 30000 + 1500 + 1000 + 800 = 33300 -> (33300 - 20833) * 0.20 = 2493.4
        assertEquals(2493.4, PayrollUtils.calculateWithholdingTax(30000, 1500, 1000, 800), DELTA);
    }
}

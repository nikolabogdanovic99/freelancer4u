package ch.zhaw.freelancer4u.model.voucher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;

public class PercentageVoucherTest {

    @Test
    public void testEmptyListGivesZero() {
        PercentageVoucher v = new PercentageVoucher(10.0);
        double discount = v.getDiscount(List.of());
        assertEquals(0.0, discount, 0.0001);
    }

    @Test
    public void testZeroPercentGivesZero() {
        PercentageVoucher v = new PercentageVoucher(0.0);
        Job j = new Job("Test", JobType.OTHER, 50.0, "1");
        assertEquals(0.0, v.getDiscount(List.of(j)), 0.0001);
    }

    @Test
    public void testTenPercent() {
        PercentageVoucher v = new PercentageVoucher(10.0);
        Job j1 = new Job("A", JobType.OTHER, 40.0, "1");
        Job j2 = new Job("B", JobType.OTHER, 10.0, "1");
        // Summe = 50 -> 10% = 5
        assertEquals(5.0, v.getDiscount(List.of(j1, j2)), 0.0001);
    }

    @Test
    public void testHundredPercent() {
        PercentageVoucher v = new PercentageVoucher(100.0);
        Job j = new Job("Full", JobType.OTHER, 12.34, "1");
        assertEquals(12.34, v.getDiscount(List.of(j)), 0.0001);
    }

    @Test
    public void testInvalidBelowZero() {
        assertThrows(IllegalArgumentException.class, () -> new PercentageVoucher(-1.0));
    }

    @Test
    public void testInvalidAboveHundred() {
        assertThrows(IllegalArgumentException.class, () -> new PercentageVoucher(100.0001));
    }
}

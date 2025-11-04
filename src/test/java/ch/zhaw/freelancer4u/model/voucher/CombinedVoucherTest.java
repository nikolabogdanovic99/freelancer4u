package ch.zhaw.freelancer4u.model.voucher;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;

public class CombinedVoucherTest {

    @Test
    void emptyJobsGiveZero() {
        CombinedVoucher cv = new CombinedVoucher(new FiveBucksVoucher(), new PercentageVoucher(10));
        assertEquals(0.0, cv.getDiscount(List.of()), 0.0001);
    }

    @Test
    void combinesAdditivelyAndCaps() {
        // Summe = 50
        Job j1 = new Job("A", JobType.OTHER, 20.0, "1");
        Job j2 = new Job("B", JobType.OTHER, 30.0, "1");

        // FiveBucks = 5, Percentage(10%) = 5 -> total 10
        CombinedVoucher cv = new CombinedVoucher(new FiveBucksVoucher(), new PercentageVoucher(10));
        assertEquals(10.0, cv.getDiscount(List.of(j1, j2)), 0.0001);
    }

    @Test
    void cappedAtSumOfEarnings() {
        // Summe = 7
        Job j = new Job("Small", JobType.OTHER, 7.0, "1");

        // Five (5) + 100% (7) = 12 -> capped to 7
        CombinedVoucher cv = new CombinedVoucher(new FiveBucksVoucher(), new PercentageVoucher(100));
        assertEquals(7.0, cv.getDiscount(List.of(j)), 0.0001);
    }

    @Test
    void zeroPercentBehavesLikeFiveOnly() {
        // Summe = 12.34
        Job j = new Job("One", JobType.OTHER, 12.34, "1");

        CombinedVoucher cv = new CombinedVoucher(new FiveBucksVoucher(), new PercentageVoucher(0));
        // FiveBucks = min(5, 12.34) = 5
        assertEquals(5.0, cv.getDiscount(List.of(j)), 0.0001);
    }

    @Test
    void nullVouchersNotAllowed() {
        assertThrows(IllegalArgumentException.class, () -> new CombinedVoucher(null, new PercentageVoucher(10)));
        assertThrows(IllegalArgumentException.class, () -> new CombinedVoucher(new FiveBucksVoucher(), null));
    }
}

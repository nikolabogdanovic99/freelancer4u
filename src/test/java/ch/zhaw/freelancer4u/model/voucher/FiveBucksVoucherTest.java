package ch.zhaw.freelancer4u.model.voucher;

import org.junit.jupiter.api.Test;                         // ✅ Richtig für JUnit 5
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.model.voucher.FiveBucksVoucher;


public class FiveBucksVoucherTest {

    @Test
    public void testUnderTen() {
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        Job job = new Job("Testjob", JobType.OTHER, 9.0, "1");
        double discount = voucher.getDiscount(List.of(job));
        assertEquals(0.0, discount);
    }

    @Test
    public void testExactlyTen() {
        FiveBucksVoucher voucher = new FiveBucksVoucher();
        Job job = new Job("Testjob", JobType.OTHER, 10.0, "1");
        double discount = voucher.getDiscount(List.of(job));
        assertEquals(5.0, discount);
    }
}

package ch.zhaw.freelancer4u.model.voucher;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;

/**
 * Tests for PercentageVoucher (Übung 8, Teil 1).
 */
public class PercentageVoucherTest {

    @ParameterizedTest
    @ValueSource(ints = {-5, -1, 0})
    void ctor_throws_when_discount_leq_zero(int value) {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> new PercentageVoucher(value));
        assertEquals("Error: Discount value must be greater zero.", ex.getMessage());
    }

    @ParameterizedTest
    @ValueSource(ints = {51, 99, 100})
    void ctor_throws_when_discount_gt_50(int value) {
        RuntimeException ex = assertThrows(RuntimeException.class, () -> new PercentageVoucher(value));
        assertEquals("Error: Discount value must less or equal 50.", ex.getMessage());
    }

    @Test
    void getDiscount_with_real_jobs() {
        PercentageVoucher voucher = new PercentageVoucher(42);
        Job job1 = new Job("title1", "desc1", JobType.IMPLEMENT, 77.0, "cid");
        Job job2 = new Job("title2", "desc2", JobType.IMPLEMENT, 42.0, "cid");
        assertEquals(49.98, voucher.getDiscount(Arrays.asList(job1, job2)), 0.01);
    }

    @Test
    void getDiscount_with_mocked_jobs_using_mockito() {
        PercentageVoucher voucher = new PercentageVoucher(10);

        Job j1 = mock(Job.class);
        when(j1.getEarnings()).thenReturn(42.0);

        Job j2 = mock(Job.class);
        when(j2.getEarnings()).thenReturn(77.0);

        List<Job> jobs = Arrays.asList(j1, j2);
        // 10% of (42 + 77) = 11.9
        assertEquals(11.9, voucher.getDiscount(jobs), 0.001);
    }
}

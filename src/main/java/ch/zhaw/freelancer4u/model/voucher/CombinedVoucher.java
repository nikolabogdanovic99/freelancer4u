package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;
import ch.zhaw.freelancer4u.model.Job;


public class CombinedVoucher {

    private final FiveBucksVoucher fiveBucksVoucher;
    private final PercentageVoucher percentageVoucher;

    public CombinedVoucher(FiveBucksVoucher fiveBucksVoucher, PercentageVoucher percentageVoucher) {
        if (fiveBucksVoucher == null || percentageVoucher == null) {
            throw new IllegalArgumentException("vouchers must not be null");
        }
        this.fiveBucksVoucher = fiveBucksVoucher;
        this.percentageVoucher = percentageVoucher;
    }

    public double getDiscount(List<Job> jobs) {
        double sum = jobs.stream().mapToDouble(Job::getEarnings).sum();
        if (sum <= 0.0) {
            return 0.0;
        }
        double total = fiveBucksVoucher.getDiscount(jobs) + percentageVoucher.getDiscount(jobs);
        return Math.min(total, sum);
    }
}

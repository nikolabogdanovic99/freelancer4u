package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;
import ch.zhaw.freelancer4u.model.Job;


public class PercentageVoucher {

    private final double percentage; 

    public PercentageVoucher(double percentage) {
        if (percentage < 0 || percentage > 100) {
            throw new IllegalArgumentException("percentage must be between 0 and 100");
        }
        this.percentage = percentage;
    }

    public double getPercentage() {
        return percentage;
    }

    public double getDiscount(List<Job> jobs) {
        double sum = jobs.stream()
                         .mapToDouble(Job::getEarnings)
                         .sum();
        return sum * (percentage / 100.0);
    }
}

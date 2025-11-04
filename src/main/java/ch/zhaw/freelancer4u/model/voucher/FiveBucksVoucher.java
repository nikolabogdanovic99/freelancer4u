package ch.zhaw.freelancer4u.model.voucher;

import java.util.List;
import ch.zhaw.freelancer4u.model.Job;

public class FiveBucksVoucher {


    public double getDiscount(List<Job> jobs) {
        double sum = jobs.stream()
                         .mapToDouble(Job::getEarnings)
                         .sum();

        if (sum >= 10) {
            return 5.0;
        } else {
            return 0.0;
        }
    }
}

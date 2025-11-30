package ch.zhaw.freelancer4u.tools;

import java.util.List;
import java.util.Optional;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ch.zhaw.freelancer4u.model.Company;
import ch.zhaw.freelancer4u.model.Job;
import ch.zhaw.freelancer4u.model.JobType;
import ch.zhaw.freelancer4u.repository.CompanyRepository;
import ch.zhaw.freelancer4u.repository.JobRepository;
import ch.zhaw.freelancer4u.service.CompanyService;

@Component
public class FreelancerTools {

    private final JobRepository jobRepository;
    private final CompanyService companyService;
    private final CompanyRepository companyRepository;

    public FreelancerTools(JobRepository jobRepository,
                           CompanyService companyService,
                           CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.companyService = companyService;
        this.companyRepository = companyRepository;
    }

    @Tool(description = "Information about the jobs in the database.")
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Tool(description = "Information about the companies in the database.")
    public List<Company> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Tool(description = "Erstellt ein neues Unternehmen mit Name und E-Mail-Adresse.")
    public Company createCompany(String name, String email) {
        companyService.createCompany(name, email);

        // Versuche die gerade erstellte Company wiederzufinden
        Optional<Company> created = companyRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst();

        return created.orElse(null);
    }

    @Tool(description = "Erstellt einen neuen Job für einen Unternehmensnamen. Legt die Firma an, falls sie noch nicht existiert.")
    public Job createJobForCompanyName(String companyName,
                                       String title,
                                       String description,
                                       JobType jobType,
                                       double earnings) {

        // Firma per Name suchen
        Optional<Company> companyOpt = companyRepository.findAll().stream()
                .filter(c -> c.getName().equalsIgnoreCase(companyName))
                .findFirst();

        // Falls nicht vorhanden neue Firma anlegen
        Company company = companyOpt.orElseGet(() -> {
            String email = companyName.toLowerCase().replace(" ", ".") + "@example.com";
            Company newCompany = new Company(companyName, email);
            return companyRepository.save(newCompany);
        });

        Job job = new Job(
                title,
                description,
                jobType,
                earnings,
                company.getId()
        );

        return jobRepository.save(job);
    }
}

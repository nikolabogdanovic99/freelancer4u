package ch.zhaw.freelancer4u.controller; 
 
import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.PostMapping; 
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RestController; 
import org.springframework.web.bind.annotation.RequestMapping; 
 
import ch.zhaw.freelancer4u.model.Company; 
import ch.zhaw.freelancer4u.model.CompanyCreateDTO; 
import ch.zhaw.freelancer4u.repository.CompanyRepository; 
 
@RestController 
@RequestMapping("/api") 
public class CompanyController { 
    @Autowired 
    CompanyRepository companyRepository; 
 
    @PostMapping("/company") 
    public ResponseEntity<Company> createCompany( 
        @RequestBody CompanyCreateDTO fDTO) { 
        Company fDAO = new Company(fDTO.getName(), fDTO.getEmail()); 
        Company f = companyRepository.save(fDAO); 
        return new ResponseEntity<>(f, HttpStatus.CREATED); 
    } 
} 
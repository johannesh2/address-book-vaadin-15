package org.vaadin.example.views.masterdetail;

import java.util.List;

import com.vaadin.flow.server.connect.Endpoint;
import com.vaadin.flow.server.connect.auth.AnonymousAllowed;
import com.vaadin.flow.server.connect.exception.EndpointException;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.example.backend.entity.Company;
import org.vaadin.example.backend.entity.Contact;
import org.vaadin.example.backend.service.CompanyService;
import org.vaadin.example.backend.service.ContactService;

/**
 * The endpoint for the client-side List View.
 */
@Endpoint
@AnonymousAllowed
public class MasterDetailEndpoint {
    private final ContactService contactService;
    private final CompanyService companyService;

    @Autowired
    public MasterDetailEndpoint(ContactService contactService,
            CompanyService companyService) {
        this.contactService = contactService;
        this.companyService = companyService;
    }

    public List<Contact> getEmployees() {
        return contactService.findAll();
    }

    public void saveEmployee(Contact contact) throws EndpointException {
        contactService.save(contact);
    }

    public List<Company> getCompanies() {
        return companyService.findAll();
    }
}

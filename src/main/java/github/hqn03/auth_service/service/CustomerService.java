package github.hqn03.auth_service.service;

import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.customer.CustomerRequest;
import github.hqn03.auth_service.dto.customer.CustomerResponse;
import github.hqn03.auth_service.mapper.CustomerMapper;
import github.hqn03.auth_service.model.Customer;
import github.hqn03.auth_service.model.User;
import github.hqn03.auth_service.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Transactional
    public void createCustomer(User user, RegisterRequest request) {
        Customer customer = customerMapper.toEntity(request);
        customer.setUser(user);
        Customer saved = customerRepository.save(customer);
        customerMapper.toResponse(saved);
    }

    @Transactional
    public CustomerResponse updateCustomer(User user, CustomerRequest customerRequest) {
        Customer customer = user.getCustomer();
        customerMapper.updateCustomerFromRequest(customerRequest, customer);
        return customerMapper.toResponse(customer);
    }
}

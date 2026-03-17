package github.hqn03.auth_service.customer.service;

import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.customer.dto.CustomerRequest;
import github.hqn03.auth_service.customer.dto.CustomerResponse;
import github.hqn03.auth_service.customer.entity.Customer;
import github.hqn03.auth_service.customer.mapper.CustomerMapper;
import github.hqn03.auth_service.customer.repository.CustomerRepository;
import github.hqn03.auth_service.user.entity.User;
import github.hqn03.auth_service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final UserRepository userRepository;

    @Transactional
    public void createCustomer(Long userId, RegisterRequest request) {
        User user = userRepository.getReferenceById(userId);
        Customer customer = customerMapper.toEntity(request);
        customer.setUser(user);
        customerRepository.save(customer);
    }

    @Transactional
    public CustomerResponse updateCustomer(User user, CustomerRequest customerRequest) {
        Customer customer = user.getCustomer();
        customerMapper.updateCustomerFromRequest(customerRequest, customer);
        return customerMapper.toResponse(customer);
    }
}

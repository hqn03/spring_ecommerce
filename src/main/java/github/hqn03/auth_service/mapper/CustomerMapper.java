package github.hqn03.auth_service.mapper;

import github.hqn03.auth_service.dto.auth.RegisterRequest;
import github.hqn03.auth_service.dto.customer.CustomerRequest;
import github.hqn03.auth_service.dto.customer.CustomerResponse;
import github.hqn03.auth_service.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(target = "phone", source = "phoneNumber")
    Customer toEntity(RegisterRequest customerRequest);

    CustomerResponse toResponse(Customer customer);

    void updateCustomerFromRequest(CustomerRequest customerRequest, @MappingTarget Customer customer);
}

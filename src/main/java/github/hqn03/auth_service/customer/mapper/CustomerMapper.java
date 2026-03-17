package github.hqn03.auth_service.customer.mapper;

import github.hqn03.auth_service.auth.dto.auth.RegisterRequest;
import github.hqn03.auth_service.customer.dto.CustomerRequest;
import github.hqn03.auth_service.customer.dto.CustomerResponse;
import github.hqn03.auth_service.customer.entity.Customer;
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

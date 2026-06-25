package com.samarth.ecommerce.customer;

import ch.qos.logback.core.util.StringUtil;
import io.micrometer.common.util.StringUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public  void updateCustomer(@Valid CustomerRequest request) {
        var customer =repository.findById(request.id())
                .orElseThrow(()->new CustomerNotFoundException(
                        String.format("Customer with id %s not found", request.id())
                ));
        mergerCustomer(customer, request);
        repository.save(customer);
    }

    public void mergerCustomer(Customer customer, @Valid CustomerRequest request) {
        if(StringUtils.isNotBlank(request.firstname())){
            customer.setFirstname(request.firstname());
        }
        if(StringUtils.isNotBlank(request.lastname())){
            customer.setLastname(request.lastname());
        }
        if(StringUtils.isNotBlank(request.email())){
            customer.setEmail(request.email());
        }
        if(request.address() != null){
            customer.setAddress(request.address());
        }
    }

    public String createCustomer(CustomerRequest request) {
        var customer=repository.save(CustomerMapper.toCustomer(request));
        return customer.getId();
    }

    public List<CustomerResponse> getAllCustomer() {
        return repository
                .findAll()
                .stream()
                .map(mapper::fromCustomer)
                .collect(Collectors.toList());
    }
//    public List<CustomerResponse> getAllCustomer() {
//
//        List<Customer> customers = repository.findAll();
//
//        List<CustomerResponse> responses = new ArrayList<>();
//
//        for(Customer customer : customers){
//
//            CustomerResponse response =
//                    mapper.fromCustomer(customer);
//
//            responses.add(response);
//        }
//
//        return responses;
//    }
//    public List<CustomerResponse> getAllCustomer() {
//
//        List<CustomerResponse> responses = new ArrayList<>();
//
//        for(Customer customer : repository.findAll()){
//
//            responses.add(
//                    mapper.fromCustomer(customer)
//            );
//
//        }
//
//        return responses;
//    }
//    public List<CustomerResponse> getAllCustomer() {
//
//        List<CustomerResponse> responses = new ArrayList<>();
//
//        repository.findAll()
//                .forEach(customer -> {
//
//                    CustomerResponse response =
//                            mapper.fromCustomer(customer);
//
//                    responses.add(response);
//
//                });
//
//        return responses;
//    }
//    public List<CustomerResponse> getAllCustomer(){
//
//        return repository.findAll()
//                .stream()
//                .map(customer ->
//                        mapper.fromCustomer(customer))
//                .collect(Collectors.toList());
//
//    }
//    public List<CustomerResponse> getAllCustomer(){
//
//        return repository.findAll()
//                .stream()
//                .map(mapper::fromCustomer)
//                .collect(Collectors.toList());
//
//    }
//    public List<CustomerResponse> getAllCustomer(){
//
//        return repository.findAll()
//                .stream()
//                .map(mapper::fromCustomer)
//                .toList();
//
//    }

    public Boolean existsById(String id) {
        return repository
                .findById(id)
                .isPresent();
    }

    public CustomerResponse findById(String customerId) {
        return repository
                .findById(customerId)
                .map(mapper::fromCustomer)
                .orElseThrow(()->new CustomerNotFoundException(String.format("Customer with id %s not found", customerId)
        ));
    }

    public void deleteById(String customerId) {
        repository.deleteById(customerId);
    }
}

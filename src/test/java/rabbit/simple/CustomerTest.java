package rabbit.simple;

import rabbit.simple.Customer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class CustomerTest {
    public static void main(String[] args) throws IOException, TimeoutException {
        Customer customer = new Customer();
        customer.customer();
    }
}

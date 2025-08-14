package br.com.grooworks.crestline.infra.config;

import com.braintreegateway.BraintreeGateway;
import com.braintreegateway.Environment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BraintreeConfig {
    @Bean
    public BraintreeGateway braintreeGateway() {
        return new BraintreeGateway(
                Environment.SANDBOX,
                "zdv5fs6kk58q5bzx", // merchantId
                "qcwzs4xqxvfxxwv9", // publicKey
                "171cd3d4b3b8bd9d3dec4ebb270bff54" // privateKey
        );
    }
}

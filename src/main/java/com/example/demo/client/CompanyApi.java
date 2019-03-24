package com.example.demo.client;

import com.example.demo.domain.Company;

import java.util.Arrays;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.boot.context.config.ResourceNotFoundException;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.netflix.feign.support.ResponseEntityDecoder;
import org.springframework.cloud.netflix.feign.support.SpringDecoder;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;
import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import feign.Contract;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;

@FeignClient(name = "companyClient" ,url = "https://api.spacexdata.com/v2/infos", configuration = CompanyApi.Config.class)
public interface CompanyApi {

    @RequestMapping(method = RequestMethod.GET)
    Company companyInfo();
    
    
    @Configuration
    class Config {
 
        @Autowired
        private ObjectFactory<HttpMessageConverters> messageConverters;

      

        @Bean
        public Decoder springDecoder() {
            return new ResponseEntityDecoder(new SpringDecoder(messageConverters));
        }

        @Bean
        public Contract feignContract() {
            return new SpringMvcContract();
        }

      

        @Bean
        public ErrorDecoder uaaErrorDecoder(Decoder decoder) {
            return (methodKey, response) -> {
                try {
                	//CustomErrorDecoder uaaException = (CustomErrorDecoder) decoder.decode(response, CustomErrorDecoder.class);
                 
                	return new ResourceNotFoundException(methodKey, null);
                	/*
                	   return new SroException(
                               response.status(),
                                response.reason(),
                               Arrays.asList(""));
                               */
                   

                } catch (Exception e) {
                    return new SroException(
                            response.status(),
                            "Authorization server responded with " + response.status() + " but failed to parse error payload",
                            Arrays.asList(e.getMessage()));
                }
            };
        }
    }


}

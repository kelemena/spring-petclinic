package org.springframework.samples.petclinic.infrastructure.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.petclinic.domain.service.ClinicService;
import org.springframework.samples.petclinic.domain.vet.VetService;
import org.springframework.samples.petclinic.domain.visit.VisitService;

@Configuration
public class MvcTestConfig {

    @Bean
    public ClinicService clinicService() {
        return Mockito.mock(ClinicService.class);
    }

    @Bean
    public VisitService visitService() {
        return Mockito.mock(VisitService.class);
    }

    @Bean
    public VetService vetService() {
        return Mockito.mock(VetService.class);
    }
}

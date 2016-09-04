/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.domain.visit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.domain.client.Pet;
import org.springframework.samples.petclinic.domain.client.ClientService;
import org.springframework.samples.petclinic.infrastructure.config.BusinessConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = BusinessConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class VisitServiceTests {

    @Autowired
    private ClientService clientService;

    @Autowired
    private VisitService visitService;

    @Test
    @Transactional
    public void shouldAddNewVisitForPet() {
        Pet pet7 = this.clientService.findPetById(7);
        int found = pet7.getVisits().size();
        Visit visit = new Visit();
        pet7.addVisit(visit);
        visit.setDescription("test");
        this.visitService.saveVisit(visit);
        this.clientService.savePet(pet7);

        pet7 = this.clientService.findPetById(7);
        assertThat(pet7.getVisits().size()).isEqualTo(found + 1);
        assertThat(visit.getId()).isNotNull();
    }

    @Test
       public void shouldFindVisitsByPetId() throws Exception {
        Collection<Visit> visits = this.visitService.findVisitsByPetId(7);
        assertThat(visits.size()).isEqualTo(2);
        Visit[] visitArr = visits.toArray(new Visit[visits.size()]);
        assertThat(visitArr[0].getPet()).isNotNull();
        assertThat(visitArr[0].getDate()).isNotNull();
        assertThat(visitArr[0].getPet().getId()).isEqualTo(7);
    }


}

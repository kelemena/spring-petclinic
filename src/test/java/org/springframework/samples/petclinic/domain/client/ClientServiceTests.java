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
package org.springframework.samples.petclinic.domain.client;

import org.joda.time.LocalDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.infrastructure.config.BusinessConfig;
import org.springframework.samples.petclinic.infrastructure.util.EntityUtils;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@ContextConfiguration(classes = BusinessConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class ClientServiceTests {

    @Autowired
    private ClientService clientService;

    @Test
    public void shouldFindOwnersByLastName() {
        Collection<Owner> owners = this.clientService.findOwnerByLastName("Davis");
        assertThat(owners.size()).isEqualTo(2);

        owners = this.clientService.findOwnerByLastName("Daviss");
        assertThat(owners.isEmpty()).isTrue();
    }

    @Test
    public void shouldFindSingleOwnerWithPet() {
        Owner owner = this.clientService.findOwnerById(1);
        assertThat(owner.getLastName()).startsWith("Franklin");
        assertThat(owner.getPets().size()).isEqualTo(1);
        assertThat(owner.getPets().get(0).getType()).isNotNull();
        assertThat(owner.getPets().get(0).getType().getName()).isEqualTo("cat");
    }

    @Test
    @Transactional
    public void shouldInsertOwner() {
        Collection<Owner> owners = this.clientService.findOwnerByLastName("Schultz");
        int found = owners.size();

        Owner owner = new Owner();
        owner.setFirstName("Sam");
        owner.setLastName("Schultz");
        owner.setAddress("4, Evans Street");
        owner.setCity("Wollongong");
        owner.setTelephone("4444444444");
        this.clientService.saveOwner(owner);
        assertThat(owner.getId().longValue()).isNotEqualTo(0);

        owners = this.clientService.findOwnerByLastName("Schultz");
        assertThat(owners.size()).isEqualTo(found + 1);
    }

    @Test
    @Transactional
    public void shouldUpdateOwner() {
        Owner owner = this.clientService.findOwnerById(1);
        String oldLastName = owner.getLastName();
        String newLastName = oldLastName + "X";

        owner.setLastName(newLastName);
        this.clientService.saveOwner(owner);

        // retrieving new name from database
        owner = this.clientService.findOwnerById(1);
        assertThat(owner.getLastName()).isEqualTo(newLastName);
    }

    @Test
    public void shouldFindPetWithCorrectId() {
        Pet pet7 = this.clientService.findPetById(7);
        assertThat(pet7.getName()).startsWith("Samantha");
        assertThat(pet7.getOwner().getFirstName()).isEqualTo("Jean");

    }

    @Test
    public void shouldFindAllPetTypes() {
        Collection<PetType> petTypes = this.clientService.findPetTypes();

        PetType petType1 = EntityUtils.getById(petTypes, PetType.class, 1);
        assertThat(petType1.getName()).isEqualTo("cat");
        PetType petType4 = EntityUtils.getById(petTypes, PetType.class, 4);
        assertThat(petType4.getName()).isEqualTo("snake");
    }

    @Test
    @Transactional
    public void shouldInsertPetIntoDatabaseAndGenerateId() {
        Owner owner6 = this.clientService.findOwnerById(6);
        int found = owner6.getPets().size();

        Pet pet = new Pet();
        pet.setName("bowser");
        Collection<PetType> types = this.clientService.findPetTypes();
        pet.setType(EntityUtils.getById(types, PetType.class, 2));
        pet.setBirthDate(new LocalDate());
        owner6.addPet(pet);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);

        this.clientService.savePet(pet);
        this.clientService.saveOwner(owner6);

        owner6 = this.clientService.findOwnerById(6);
        assertThat(owner6.getPets().size()).isEqualTo(found + 1);
        // checks that id has been generated
        assertThat(pet.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void shouldUpdatePetName() throws Exception {
        Pet pet7 = this.clientService.findPetById(7);
        String oldName = pet7.getName();

        String newName = oldName + "X";
        pet7.setName(newName);
        this.clientService.savePet(pet7);

        pet7 = this.clientService.findPetById(7);
        assertThat(pet7.getName()).isEqualTo(newName);
    }

}

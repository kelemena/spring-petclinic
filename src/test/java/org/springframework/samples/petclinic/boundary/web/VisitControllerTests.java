package org.springframework.samples.petclinic.boundary.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.infrastructure.config.MvcCoreConfig;
import org.springframework.samples.petclinic.infrastructure.config.MvcTestConfig;
import org.springframework.samples.petclinic.domain.client.Pet;
import org.springframework.samples.petclinic.domain.client.ClientService;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for {@link VisitController}
 *
 * @author Colin But
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = { MvcCoreConfig.class, MvcTestConfig.class })
public class VisitControllerTests {

    private static final int TEST_PET_ID = 1;

    @Autowired
    private VisitController visitController;

    @Autowired
    private ClientService clientService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(visitController).build();

        given(this.clientService.findPetById(TEST_PET_ID)).willReturn(new Pet());
    }

    @Test
    public void testInitNewVisitForm() throws Exception {
        mockMvc.perform(get("/owners/*/pets/{petId}/visits/new", TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    public void testProcessNewVisitFormSuccess() throws Exception {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
            .param("name", "George")
            .param("description", "Visit Description")
        )
            .andExpect(status().is3xxRedirection())
            .andExpect(view().name("redirect:/owners/{ownerId}"));
    }

    @Test
    public void testProcessNewVisitFormHasErrors() throws Exception {
        mockMvc.perform(post("/owners/*/pets/{petId}/visits/new", TEST_PET_ID)
            .param("name", "George")
        )
            .andExpect(model().attributeHasErrors("visit"))
            .andExpect(status().isOk())
            .andExpect(view().name("pets/createOrUpdateVisitForm"));
    }

    @Test
    public void testShowVisits() throws Exception {
        mockMvc.perform(get("/owners/*/pets/{petId}/visits", TEST_PET_ID))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("visits"))
            .andExpect(view().name("visitList"));
    }


}
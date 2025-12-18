package ro.univ.medical.gestiune_pacienti;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.unauthenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigurationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testPublicAccessToLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "doctor", roles = {"EDITOR"})
    void testAuthenticatedAccessToHome() throws Exception {
        mockMvc.perform(get("/pacienti"))
                .andExpect(status().isOk());
    }

    @Test
    void testUnauthenticatedAccessToProtectedPage_ShouldRedirectToLogin() throws Exception {
        mockMvc.perform(get("/pacienti"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    void testFormLogin_Success() throws Exception {
        // Acest test presupune că ai utilizatori în baza de date de test
        // Dacă nu, poți să-l comentezi sau să-l adaptezi
        mockMvc.perform(formLogin("/login")
                        .user("username", "teodora25")
                        .password("password", "editor123"))
                .andExpect(authenticated());
    }

    @Test
    void testFormLogin_Failure() throws Exception {
        mockMvc.perform(formLogin("/login")
                        .user("username", "unknown")
                        .password("password", "wrong"))
                .andExpect(unauthenticated());
    }
}
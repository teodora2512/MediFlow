package ro.univ.medical.gestiune_pacienti.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ro.univ.medical.gestiune_pacienti.entity.Pacient;
import ro.univ.medical.gestiune_pacienti.service.PacientService;

import java.util.List;

@Controller
@RequestMapping("/pacienti")
public class PacientWebController {

    private final PacientService pacientService;

    public PacientWebController(PacientService pacientService) {
        this.pacientService = pacientService;
    }

    /**
     * Metodă helper pentru a obține utilizatorul curent
     */
    private Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * Metodă helper pentru a verifica dacă utilizatorul este editor
     */
    private boolean isCurrentUserEditor() {
        Authentication auth = getCurrentAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_EDITOR"));
    }

    /**
     * Metodă helper pentru a seta toate atributele comune ale modelului
     */
    private void setCommonModelAttributes(Model model,
                                          String cnp,
                                          String nume,
                                          String prenume,
                                          String diagnostic,
                                          String tratament,
                                          String mesaj,
                                          List<Pacient> listaPacienti) {

        Authentication auth = getCurrentAuthentication();
        String username = auth.getName();

        model.addAttribute("welcome", "Bun venit, " + username + "!");
        model.addAttribute("str", mesaj != null ? mesaj : "Lista pacienților înregistrați");
        model.addAttribute("lista", listaPacienti);
        model.addAttribute("isEditor", isCurrentUserEditor());

        // Setează parametrii pentru formular
        model.addAttribute("cnpParam", cnp != null ? cnp : "");
        model.addAttribute("numeParam", nume != null ? nume : "");
        model.addAttribute("prenumeParam", prenume != null ? prenume : "");
        model.addAttribute("diagnosticParam", diagnostic != null ? diagnostic : "");
        model.addAttribute("tratamentParam", tratament != null ? tratament : "");
    }

    @GetMapping
    public String getListaPacienti(Model model,
                                   @RequestParam(name = "mesaj", required = false) String mesaj,
                                   @RequestParam(name = "cnpParam", required = false) String cnpParam,
                                   @RequestParam(name = "numeParam", required = false) String numeParam,
                                   @RequestParam(name = "prenumeParam", required = false) String prenumeParam,
                                   @RequestParam(name = "diagnosticParam", required = false) String diagnosticParam,
                                   @RequestParam(name = "tratamentParam", required = false) String tratamentParam) {

        List<Pacient> listaPacienti = pacientService.findAll();
        setCommonModelAttributes(model, cnpParam, numeParam, prenumeParam, diagnosticParam, tratamentParam,
                mesaj, listaPacienti);

        return "pacienti";
    }

    @PostMapping("/operatii")
    public String operatiiPacient(
            @RequestParam(name = "cnp", required = false) String cnp,
            @RequestParam(name = "nume", required = false) String nume,
            @RequestParam(name = "prenume", required = false) String prenume,
            @RequestParam(name = "diagnostic", required = false) String diagnostic,
            @RequestParam(name = "tratament", required = false) String tratament,
            @RequestParam(name = "adauga", required = false) String adauga,
            @RequestParam(name = "sterge", required = false) String sterge,
            @RequestParam(name = "cauta", required = false) String cauta,
            @RequestParam(name = "modifica", required = false) String modifica,
            @RequestParam(name = "externare", required = false) String externare,
            Model model) {

        String mesaj = "";
        List<Pacient> listaPacienti;
        boolean isEditor = isCurrentUserEditor();

        try {
            // --- OPERAȚII CRUD ---
            if (adauga != null) {
                mesaj = handleAdauga(cnp, nume, prenume, diagnostic, tratament, isEditor);
                listaPacienti = pacientService.findAll();
            } else if (externare != null) {
                mesaj = handleExternare(cnp, isEditor);
                listaPacienti = pacientService.findAll();
            } else if (modifica != null) {
                mesaj = handleModifica(cnp, nume, prenume, diagnostic, tratament, isEditor);
                listaPacienti = pacientService.findAll();
            } else if (sterge != null) {
                mesaj = handleSterge(cnp, isEditor);
                listaPacienti = pacientService.findAll();
            } else if (cauta != null) {
                SearchResult searchResult = handleCauta(cnp, nume, prenume, diagnostic, tratament, model);
                mesaj = searchResult.mesaj();
                listaPacienti = searchResult.listaPacienti();
            } else {
                mesaj = "Lista pacienților preluate";
                listaPacienti = pacientService.findAll();
            }
        } catch (Exception e) {
            mesaj = "A apărut o eroare: " + e.getMessage();
            listaPacienti = pacientService.findAll();
        }

        setCommonModelAttributes(model, cnp, nume, prenume, diagnostic, tratament, mesaj, listaPacienti);
        return "pacienti";
    }

    // --- Metode helper pentru operații individuale ---

    private String handleAdauga(String cnp, String nume, String prenume, String diagnostic,
                                String tratament, boolean isEditor) {
        if (!isEditor) {
            return "Nu aveți permisiunea de a adăuga pacienți!";
        }
        return pacientService.adaugaPacient(cnp, nume, prenume, diagnostic, tratament);
    }

    private String handleExternare(String cnp, boolean isEditor) {
        if (!isEditor) {
            return "Nu aveți permisiunea de a externa pacienți!";
        }
        return pacientService.externarePacient(cnp);
    }

    private String handleModifica(String cnp, String nume, String prenume, String diagnostic,
                                  String tratament, boolean isEditor) {
        if (!isEditor) {
            return "Nu aveți permisiunea de a modifica pacienți!";
        }
        return pacientService.modificaPacient(cnp, nume, prenume, diagnostic, tratament);
    }

    private String handleSterge(String cnp, boolean isEditor) {
        if (!isEditor) {
            return "Nu aveți permisiunea de a șterge pacienți!";
        }
        return pacientService.stergePacient(cnp);
    }

    private SearchResult handleCauta(String cnp, String nume, String prenume,
                                     String diagnostic, String tratament, Model model) {

        List<Pacient> listaPacienti = pacientService.cautaPacienti(
                cnp, nume, prenume, diagnostic, tratament
        );

        if (listaPacienti.isEmpty()) {
            return new SearchResult("Nu există pacienți care să corespundă criteriilor introduse.",
                    listaPacienti);
        }

        // Dacă s-a căutat după CNP și s-a găsit exact un pacient, precompletează formularul
        if (cnp != null && !cnp.isBlank() && listaPacienti.size() == 1) {
            Pacient pacientGasit = listaPacienti.get(0);
            model.addAttribute("cnpParam", cnp);
            model.addAttribute("numeParam", pacientGasit.getNume());
            model.addAttribute("prenumeParam", pacientGasit.getPrenume());
            model.addAttribute("diagnosticParam", pacientGasit.getDiagnostic());
            model.addAttribute("tratamentParam", pacientGasit.getTratament());

            return new SearchResult("Pacient găsit. Puteți modifica datele.", listaPacienti);
        }

        StringBuilder descriere = new StringBuilder("Pacienți găsiți");
        appendCriteriu(descriere, "nume", nume);
        appendCriteriu(descriere, "prenume", prenume);
        appendCriteriu(descriere, "diagnostic", diagnostic);
        appendCriteriu(descriere, "tratament", tratament);

        return new SearchResult(descriere.toString(), listaPacienti);
    }

    private void appendCriteriu(StringBuilder builder, String criteriu, String valoare) {
        if (valoare != null && !valoare.isBlank()) {
            builder.append(" cu ").append(criteriu).append("=").append(valoare);
        }
    }

    private record SearchResult(String mesaj, List<Pacient> listaPacienti) {}
}
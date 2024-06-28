package com.example.securitymedianet.Controllers;

import com.example.securitymedianet.Services.Flask.IFlaskServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
@RequestMapping("/prediction")
@RestController
public class FlaskController {

    @Autowired
    private IFlaskServices flaskService;

    @GetMapping("/all")
    public Map<String, Object> getPrediction(@RequestParam Float analysteConcepteur, @RequestParam Float gestionCoordination) {
        return flaskService.predictAll(analysteConcepteur, gestionCoordination);
    }
    @GetMapping("/IC")
    public Map<String, Object> getPredictionIC(@RequestParam Float analysteConcepteur, @RequestParam Float gestionCoordination) {
        return flaskService.predictIC(analysteConcepteur, gestionCoordination);
    }
    @GetMapping("/IS")
    public Map<String, Object> getPredictionIS(@RequestParam Float gestionCoordination) {
        return flaskService.predictIS( gestionCoordination);
    }
    @GetMapping("/IT")
    public Map<String, Object> getPredictionIT(@RequestParam Float gestionCoordination, @RequestParam Float ingenieur_systeme) {
        return flaskService.predictIT(gestionCoordination, ingenieur_systeme);
    }
    @GetMapping("/SEO")
    public Map<String, Object> getPredictionSEO(@RequestParam Float analysteConcepteur, @RequestParam Float gestionCoordination) {
        return flaskService.predictSEO(analysteConcepteur, gestionCoordination);
    }
    @GetMapping("/formation")
    public Map<String, Object> getPredictionFormation( @RequestParam Float gestionCoordination) {
        return flaskService.predictFormation(gestionCoordination);
    }
    @GetMapping("/integration")
    public Map<String, Object> getPredictionIntegration(@RequestParam Float ingenieur_test,@RequestParam Float ingenieur_systeme, @RequestParam Float infographie) {
        return flaskService.predictIntegration(ingenieur_test,ingenieur_systeme, infographie);
    }
    @GetMapping("/infographie")
    public Map<String, Object> getPredictionInfographie(@RequestParam Float analysteConcepteur,@RequestParam Float insertion_contenu) {
        return flaskService.predictInfographie(analysteConcepteur,insertion_contenu);
    }

}

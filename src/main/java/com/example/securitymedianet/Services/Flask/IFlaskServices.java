package com.example.securitymedianet.Services.Flask;

import java.util.Map;

public interface IFlaskServices {
    Map<String, Object> predictAll(float analysteConcepteur, float gestionCoordination);

    Map<String, Object> predictIC(float analysteConcepteur, float gestionCoordination);

    Map<String, Object> predictIS(float gestionCoordination);

    Map<String, Object> predictIT(float gestionCoordination, float ingenieur_systeme);

    Map<String, Object> predictSEO(float analysteConcepteur, float gestionCoordination);

    Map<String, Object> predictIntegration(float ingenieur_test, float ingenieur_systeme, float infographie);

    Map<String, Object> predictInfographie(float analysteConcepteur, float insertion_contenu);

    Map<String, Object> predictFormation(float gestionCoordination);
}

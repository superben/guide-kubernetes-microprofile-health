// tag::copyright[]
/*******************************************************************************
 * Copyright (c) 2019, 2020 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - Initial implementation
 *******************************************************************************/
// end::copyright[]
package io.openliberty.guides.inventory;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.health.Readiness;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

@Readiness
@ApplicationScoped
public class InventoryReadinessCheck implements HealthCheck {
    
    private static final String readinessCheck = InventoryResource.class.getSimpleName() 
                                                 + " Readiness Check";

    @Inject
    @ConfigProperty(name = "SYS_APP_HOSTNAME")
    private String hostname;

    public HealthCheckResponse call() {
        if (isSystemServiceReachable()) {
            return HealthCheckResponse.up(readinessCheck);
        } else {
            return HealthCheckResponse.down(readinessCheck);
        }
    }
    
    private boolean isSystemServiceReachable() {
        try {
            Client client = ClientBuilder.newClient();
            client
                .target("http://" + hostname + ":9080/system/properties")
                .request()
                .post(null);

            return true;
        } catch (Exception ex) {
            return false;
        }
    }
}

package com.example.thekoguryo.oci.samples.oke;

import com.oracle.bmc.ConfigFileReader;

import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.auth.okeworkloadidentity.OkeWorkloadIdentityAuthenticationDetailsProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OCIConfig {

    private static String OCI_AUTH = System.getenv().get("OCI_AUTH");
    private static String OCI_CONFIG_FILE = System.getenv().get("OCI_CONFIG_FILE");
    public  static String OCI_REGION = System.getenv().get("OCI_REGION");

    private static Logger logger = LoggerFactory.getLogger(OCIConfig.class);

    static {
        logger.info("## OCI_AUTH: " + OCI_AUTH);
        logger.info("## OCI_CONFIG_FILE: " + OCI_CONFIG_FILE);
        logger.info("## OCI_REGION: " + OCI_REGION);      
    }

    public static AbstractAuthenticationDetailsProvider getAuthenticationDetailsProvider() throws Exception {
        String configurationFilePath = "~/.oci/config";
        String profile = "DEFAULT";

        if (OCI_AUTH != null && OCI_AUTH.compareTo("OkeWorkloadIdentity") == 0 ) {
            /* Config the Container Engine for Kubernetes workload identity provider */
            OkeWorkloadIdentityAuthenticationDetailsProvider provider = new OkeWorkloadIdentityAuthenticationDetailsProvider
                .OkeWorkloadIdentityAuthenticationDetailsProviderBuilder()
                .build();            

            return provider;
        } else {
            if (OCI_CONFIG_FILE != null) {
                configurationFilePath = OCI_CONFIG_FILE;
            }

            ConfigFileReader.ConfigFile configFile = ConfigFileReader.parse(configurationFilePath, profile);

            if (OCI_REGION == null) {
                OCI_REGION = configFile.get("region");
            }
            
            AuthenticationDetailsProvider provider = new ConfigFileAuthenticationDetailsProvider(configFile);

            return provider;
        }
    }
}
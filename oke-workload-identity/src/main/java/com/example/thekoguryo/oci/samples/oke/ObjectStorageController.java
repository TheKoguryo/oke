package com.example.thekoguryo.oci.samples.oke;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ObjectStorageController {
    
    @RequestMapping("/namespace")
    public String getNamespace() throws Exception {
        AbstractAuthenticationDetailsProvider provider = OCIConfig.getAuthenticationDetailsProvider();
        
        /* Configure the client to use workload identity provider*/
        ObjectStorage client = ObjectStorageClient.builder().region(OCIConfig.OCI_REGION).build(provider);

        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());
        String namespaceName = namespaceResponse.getValue();
        System.out.println("Using namespace: " + namespaceName);

        return "{ \"data\": \"" + namespaceName + "\" }\n";

    }
    
}

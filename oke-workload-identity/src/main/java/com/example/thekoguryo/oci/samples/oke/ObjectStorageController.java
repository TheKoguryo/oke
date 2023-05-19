package com.example.thekoguryo.oci.samples.oke;

import java.util.List;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.AuthenticationDetailsProvider;
import com.oracle.bmc.auth.AbstractAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorage;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.ListObjectsRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.ListObjectsResponse;

import com.oracle.bmc.objectstorage.model.ObjectSummary;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class ObjectStorageController {
    
    @RequestMapping("/objects")
    public String getObjects(@RequestParam(value = "bucket_name") String bucketName) throws Exception {
        AbstractAuthenticationDetailsProvider provider = OCIConfig.getAuthenticationDetailsProvider();
        
        /* Configure the client to use workload identity provider*/
        ObjectStorage client = ObjectStorageClient.builder().region(OCIConfig.OCI_REGION).build(provider);

        GetNamespaceResponse namespaceResponse = client.getNamespace(GetNamespaceRequest.builder().build());
        String namespaceName = namespaceResponse.getValue();        

        // fetch the file from the object storage
        ListObjectsRequest request = ListObjectsRequest.builder()
                                                        .namespaceName(namespaceName)
                                                        .bucketName(bucketName).build();
        ListObjectsResponse response = client.listObjects(request);
        List<ObjectSummary> objectList = response.getListObjects().getObjects();

        StringBuilder jsonBody = new StringBuilder();
        jsonBody.append("{\n");
        jsonBody.append("  \"data\": [\n");
        for(ObjectSummary object: objectList) {
            jsonBody.append("    {\n");
            jsonBody.append("      \"name\": \"" + object.getName() + "\"\n");
            jsonBody.append("    },\n");
        }
        jsonBody.deleteCharAt(jsonBody.lastIndexOf(","));
        jsonBody.append("  ]\n");        
        jsonBody.append("}\n");        

        System.out.println(jsonBody);

        client.close();

        return jsonBody.toString();
    }    
}

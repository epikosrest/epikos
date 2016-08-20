package core.dynamicrestresources.etc;

import org.glassfish.jersey.server.model.Resource;

import java.util.List;

/**
 * Created by nitina on 5/14/16.
 */
public class ResourceDocumentBuilder {

    private StringBuilder resourceDocument;
    private StringBuilder resourceInvalidApiDocument;
    public static final String docHeader = "<html><head><title>Resource API Documents</title><style>table, th, td {" +
        "    border: 1px solid black;" +
                "}</style></head><body><h1>API Doc</h1><table> <tr><td> Resource Path </td><td> Resource Method </td><td> Resource Consume Type </td><td> Resource Produces Type </td></tr>";
    public static final String docFooter = "</table></body></html>";

    public ResourceDocumentBuilder(){
        resourceDocument = new StringBuilder();
        resourceInvalidApiDocument = new StringBuilder();
    }

    /***
     * Create Table view of API documents
     * @param resourceList
     */
    public void addResourceValidInformation(List<Resource> resourceList,String serviceURI){
        resourceDocument.append(docHeader);

        for(Resource resource : resourceList){
            addResourceValidInformation(resource,serviceURI);
        }

        if(resourceInvalidApiDocument != null && resourceInvalidApiDocument.length()!=0){
            resourceDocument.append(resourceInvalidApiDocument.toString());
        }

        resourceDocument.append(docFooter);

    }

    private void addResourceValidInformation(Resource resource,String serviceURI){

        resourceDocument.append("<tr><td bgcolor=\"#0A5F07\">");
        resourceDocument.append(serviceURI + resource.getPath());
        resourceDocument.append("</td><td bgcolor=\"#0A5F07\">");
        resourceDocument.append(resource.getResourceMethods().get(0).getHttpMethod());
        resourceDocument.append("</td><td bgcolor=\"#0A5F07\">");
        resourceDocument.append(resource.getResourceMethods().get(0).getConsumedTypes().get(0).toString());
        resourceDocument.append("</td><td bgcolor=\"#0A5F07\">");
        resourceDocument.append(resource.getResourceMethods().get(0).getProducedTypes().get(0).toString());
        resourceDocument.append("</td></tr>");
    }

    public void addResourceInvalidInformation(String invalidInfo){
        resourceInvalidApiDocument.append("<tr><td colspan=\"4\" bgcolor=\"#0A5F07\">");
        resourceInvalidApiDocument.append(invalidInfo);
        resourceInvalidApiDocument.append("</td></tr>");
    }

    public void updateResourceInvalidInformation(String invalidInfo){
        resourceInvalidApiDocument.append("</br><tr><td colspan=\"4\" bgcolor=\"#0A5F07\">");
        resourceInvalidApiDocument.append("<b>" + invalidInfo + "</b>");
        resourceInvalidApiDocument.append("</td></tr>");
    }

    public String getResourceDocument() {
        return resourceDocument.toString();
    }

}

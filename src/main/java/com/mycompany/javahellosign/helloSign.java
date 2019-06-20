/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.javahellosign;

import com.hellosign.sdk.HelloSignClient;
import com.hellosign.sdk.HelloSignException;
import com.hellosign.sdk.resource.Account;
import com.hellosign.sdk.resource.SignatureRequest;
import com.hellosign.sdk.resource.support.SignatureRequestList;
import com.hellosign.sdk.resource.Team;

import com.hellosign.sdk.resource.EmbeddedRequest;
import com.hellosign.sdk.resource.EmbeddedResponse;
import com.hellosign.sdk.resource.AbstractRequest;
import com.hellosign.sdk.resource.support.Signature;

import com.hellosign.sdk.resource.TemplateSignatureRequest;
import com.hellosign.sdk.resource.Template;
import com.hellosign.sdk.resource.support.TemplateList;
import com.hellosign.sdk.resource.TemplateDraft;

import java.util.Scanner;
import java.io.File;
import java.util.List;
import org.json.*;
/**
 *
 * @author nicboutte
 */
public class helloSign {
    public static void main(String[] args) throws HelloSignException, JSONException {
        System.out.println("HelloSign World!");

        helloSign.menu();
        helloSign.getInput();
    }
    
    static void menu() throws HelloSignException, JSONException {      
        System.out.println("------------ACCOUNT------------");
        System.out.println("1 - Get Account");
        System.out.println("2 - Update Account");
        System.out.println("------------TEAM------------");
        System.out.println("3 - Get Team");
        System.out.println("------------SIGNATURE REQUEST------------");
        System.out.println("4 - Get Signature Request");
        System.out.println("5 - List Signature Requests");
        System.out.println("6 - Send Signature Request");
        System.out.println("7 - Send Signature Request with Template");
        System.out.println("8 - Get Files");
        System.out.println("9 - Send Embedded Signature Request");
        System.out.println("10 - Send Embedded Signature Request with Template");
        System.out.println("------------TEMPLATES------------");
        System.out.println("11 - Get Template");
        System.out.println("12 - List all templates");
        System.out.println("13 - Create Embedded Template Draft");
    }
    
    static void getInput() throws HelloSignException, JSONException {
        Scanner scanner = new Scanner(System.in);
        int selection = scanner.nextInt();
        scanner.close();
        
        switch (selection) {
            case 1: accountInfo();
            case 2: updateAccount();
            case 3: teamInfo();
            case 4: getRequest();
            case 5: listRequests();
            case 6: sendSignatureRequest();
            case 7: sendTemplate();
            case 8: download();
            case 9: embeddedRequest();
            case 10: embeddedTemplateRequest();
            case 11: templateInfo();
            case 12: listTemplates();
            case 13: embeddedDraft();
                break;
            default: break;
        }
        
    }
    
    static void accountInfo() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        Account account = client.getAccount();
        System.out.println(account);
    }
    
    static void updateAccount() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        client.setCallback("https://www.example.com/callback");
    }
    
    static void teamInfo() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        Team team = client.getTeam();
        
        System.out.println(team);
    }
    
    static void getRequest() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        String signatureRequestId = "09c620fa0af96eaee4149b9292bb8cd192fa4a80";
        SignatureRequest request = client.getSignatureRequest(signatureRequestId);
        
        System.out.println(request);
    }
    
    static void listRequests() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        SignatureRequestList list = client.getSignatureRequests(1);
        
        System.out.println(list);
    }
    
    static void sendSignatureRequest() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        SignatureRequest request = new SignatureRequest();
        request.setSubject("NDA");
        request.setTestMode(true);
        request.setMessage("Hi Gene, Please sign this NDA and let's discuss.");
        request.addSigner("nicholas.boutte+1@hellosign.com", "Gene Belcher");
        request.addFile(new File("/Users/nicboutte/Downloads/NDA.pdf"));
        request.addFile(new File("/Users/nicboutte/Downloads/vpn.png"));
        request.addFile(new File("/Users/nicboutte/Downloads/Salary__1__UPDATED_to_PDF.pdf"));

        SignatureRequest response = client.sendSignatureRequest(request);
        System.out.println(response.toString());       
    }
    
    static void sendTemplate() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        TemplateSignatureRequest request = new TemplateSignatureRequest();
        request.setTemplateId("85eb6d1cdbca7780f9e875697c36bc762e0fec67");
        request.addFile(new File("/Users/nicboutte/Downloads/222222.png"));
        request.setSubject("Bob's Burgers");
        request.setMessage("Bob's Burgers");
        request.setSigner("1", "nicholas.boutte+1@hellosign.com", "Bob belcher");
        request.setRedirectUrl("http://www.google.com");
        request.setTestMode(true);
        
        SignatureRequest newRequest = client.sendTemplateSignatureRequest(request);
    }
    
    static void download() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        String signatureRequestId = "220d0f9bbccd8ee3b148474c84afa19612e2be64";
        File zippedFiles = client.getFiles(signatureRequestId, SignatureRequest.SIGREQ_FORMAT_ZIP);
    }
    
    static void embeddedRequest() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");

        SignatureRequest request = new SignatureRequest();
        request.setTitle("embedded signature request from Java");
        request.setTestMode(true);
        request.addSigner("nicholas.boutte+1@hellosign.com", "Jill");
        request.addFileUrl("http://www.orimi.com/pdf-test.pdf");
        
        String clientId = "44adc0a3f5f6b105ca6386e164d1e87a";
        EmbeddedRequest embedReq = new EmbeddedRequest(clientId, request);
        
        SignatureRequest newRequest = (SignatureRequest) client.createEmbeddedRequest(embedReq);
        
        Signature signature = newRequest.getSignatures().get(0);
        
        String signatureId = signature.getId();
        
        EmbeddedResponse response = client.getEmbeddedSignUrl(signatureId);
        String url = response.getSignUrl();       
        
        System.out.println(url);
    }
    
    static void embeddedTemplateRequest() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");

        TemplateSignatureRequest request = new TemplateSignatureRequest();
        request.setTemplateId("58b76c5b04888dd7ed0359c887256a0334e705e0");
        request.setSigner("signer", "nicholas.boutte+1@hellosign.com", "Burgerboss");
        request.setTestMode(true);
        
        String clientId = "44adc0a3f5f6b105ca6386e164d1e87a";
        
        EmbeddedRequest embedReq = new EmbeddedRequest(clientId, request);
        
        SignatureRequest newRequest = (SignatureRequest) client.createEmbeddedRequest(embedReq);
  
        Signature signature = newRequest.getSignatures().get(0);
        
        String signatureId = signature.getId();
        
        EmbeddedResponse response = client.getEmbeddedSignUrl(signatureId);
        String url = response.getSignUrl();       
        
        System.out.println(url);     
    }
    
    static void templateInfo() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        Template template = client.getTemplate("85eb6d1cdbca7780f9e875697c36bc762e0fec67");
        
        System.out.println(template);
    }
    
    static void listTemplates() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        TemplateList templateList = client.getTemplates(1);
        
        System.out.println(templateList);
    }
    
    static void embeddedDraft() throws HelloSignException {
        HelloSignClient client = new HelloSignClient("API KEY");
        
        TemplateDraft draft = new TemplateDraft();
        draft.setTestMode(true);
        draft.setTitle("Test Java Template");
        draft.addSignerRole("Client");
        draft.addFileUrl("http://www.orimi.com/pdf-test.pdf");
        
        String clientId = "44adc0a3f5f6b105ca6386e164d1e87a";
        
        EmbeddedRequest embedReq = new EmbeddedRequest(clientId, draft);
        
        TemplateDraft temp = client.createEmbeddedTemplateDraft(embedReq);
        
        String editUrl = temp.getEditUrl();
        
        System.out.println(editUrl);
    }
}
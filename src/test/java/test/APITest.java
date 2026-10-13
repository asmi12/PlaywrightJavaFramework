package test;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;

public class APITest {

    @Test
    public void e2eAPiTest()
    {

        //Login
        HashMap<Object,Object> loginPayload=new HashMap<>();
        loginPayload.put( "email" ,"rahulshetty1@yahoo.com");
         loginPayload.put("password","Magiclife1!");
        Playwright playwright=Playwright.create();
        APIRequestContext apiRequest= playwright.request().newContext();
        APIResponse loginResponse=apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/auth/login",
                RequestOptions.create().setData(loginPayload));
        Assert.assertTrue(loginResponse.ok());
        System.out.println(loginResponse.text());
       String token= JsonPath.read(loginResponse.text(),"$.token");//extrct json respose
        System.out.println("Login Success"+token);

        //create event
        HashMap<Object,Object> createEventPayload= new HashMap<>();
        createEventPayload.put("title","API Tst Event");
        createEventPayload.put("description","Test api");
        createEventPayload.put( "category","Concert");
        createEventPayload.put( "venue", "Test");
        createEventPayload.put("city", "Pune");
        createEventPayload.put("eventDate", "2026-09-29T05:41:00.000Z");
        createEventPayload.put( "price", "100");
        createEventPayload.put( "totalSeats", "500");

       APIResponse eventResponse= apiRequest.post("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization","Bearer " +token)
                        .setData(createEventPayload));
       Assert.assertTrue(eventResponse.ok(),"create event api success");
       int eventId=JsonPath.read(eventResponse.text(),"$.data.id");
       System.out.println("event created id is"+eventId);

       //Getevent
        APIResponse retrieveEvents=apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setQueryParam("page","1").setQueryParam("limit","12")
                        .setHeader("Authorization","Bearer " +token));
        Assert.assertTrue(retrieveEvents.ok(),"Event retrival api success");
        List<Integer> allEventId=JsonPath.read(retrieveEvents.text(),"$.data[*].id");//retrive all events
        Assert.assertTrue(allEventId.contains(eventId),"Created event should appear in list");

        //delete event
       APIResponse deleteRespose= apiRequest.delete("https://api.eventhub.rahulshettyacademy.com/api/events/"+eventId,
                RequestOptions.create().setHeader("Authorization","Bearer " +token));
       Assert.assertTrue(deleteRespose.ok());

       //verify deletion is success
        APIResponse verifyRes=apiRequest.get("https://api.eventhub.rahulshettyacademy.com/api/events",
                RequestOptions.create().setHeader("Authorization","Bearer " +token)
                        .setQueryParam("page","1")
                        .setQueryParam("limit","12"));
        Assert.assertTrue(verifyRes.ok(),"post Deleted event list call success");

       List<String> idAfterdeletr= JsonPath.read(verifyRes.text(),"$.data[*].id");
       Assert.assertFalse(idAfterdeletr.contains(eventId),"Deleted evevt no longer availabele");
       System.out.println("Deletion verifies");


    }
}

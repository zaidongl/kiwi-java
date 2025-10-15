@api @rest
Feature: example API testing
    As a user
    I want to verify that the example API is working correctly

    Scenario: default greeting API - positive case
        And I have headers "@header" as below
            | Content-Type | application/json |
        Then "RestAgent" send a "GET" request to endpoint "/greet" with "@header", "" and ""
        Then Get response as "@response"
        Then The "@response" status code should be 200
        Then The "@response" body should contain "Hello Kiwi!"

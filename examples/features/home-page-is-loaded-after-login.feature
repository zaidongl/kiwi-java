@home-page
Feature: Home Page is loaded after login
    As a user
    I want to verify that the home page is loaded after login

    Scenario: login with invalid credentials
        When "BrowserAgent" opens page "http://localhost:3000"
        Then "BrowserAgent" should be on page "Login-Page"
        When "BrowserAgent" type "test-user" into "Login-Page.user-textbox"
        And "BrowserAgent" type ciphertext "5035466170666f71707543666377412f5844743372773d3d" into "Login-Page.password-textbox"
        Then "BrowserAgent" clicks on "Login-Page.signin-button"
        Then "BrowserAgent" sees "Username or password is invalid" on "Login-Page.alert-label"
        Then "BrowserAgent" closes the browser

    Scenario: login with valid credentials
        When "BrowserAgent" opens page "http://localhost:3000"
        Then "BrowserAgent" should be on page "Login-Page"
        When "BrowserAgent" type "test-user" into "Login-Page.user-textbox"
        And "BrowserAgent" type ciphertext "336676614c673549446c7665737775587a68565163773d3d" into "Login-Page.password-textbox"
        Then "BrowserAgent" clicks on "Login-Page.signin-button"
        Then "BrowserAgent" should be on page "Home-Page"
        Then "BrowserAgent" closes the browser

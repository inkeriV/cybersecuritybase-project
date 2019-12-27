# Description
The app is a simple, very flawed and badly designed app for signing up to a 'by invite only' party. Attending guests can sign in with a unique username, their name, phone number and email address, and to submit their registration they need to give the password they have been given with the invite. After that the guest can view their own sign up if they wish to. Admins are (supposedly) required to sign in on the log in page with a username and a password. Admin users can view all guests and delete registrations.

For trying the app, the user credentials for guest and admin are
* guest: password: ```w3Lc0M32p4rTy!```
* admin: username: ```admin```, password: ```a4tG2ivnk70q9```

# Raport

## FLAW 1: Sensitive data exposure

**Description**: After signing up a guest can view their information on the page by submitting their unique username on the search bar. Even though when signing up a unique username that separates guests from each other is required searching up information doesn't require any authentication or even a sign up. This gives attackers a chance to freely brute force through different combinations by fuzzing for example. If the attacker succeeds, he/she can see the guests name, phone number and email address.

**How to fix**: To have only the rightful user be able to see their data would require a proper logging in. An easy fix would be to also require a password when signing up, and ask for the username - password combination before showing any guests information. A more better way is to implement an actual security configuration that the Spring framework provides.


## FLAW 2: SQL injection





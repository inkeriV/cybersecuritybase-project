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

**Description**: Attacker can perform a sql injection attack in the search bar that is for the guest to search their own username and see their information. For example, if an attacker writes:```' or 1='1``` in the search bar, they can see all the guests that have signed up for the party and their data. Now the app uses Java Persistence to map java objects to database and the other way round. Java persistence has a entity manager that can create queries that are injectable. The queries are like: ```"select * from Guest where username = '" + username + "'"``` and therefore whatever can be passed as a username string and it is put into the query.

**How to fix**: To do the same job safely the app should implement a JPA repository. The Guest class should extend a AbstractPersistable superclass: ```public class Guest extends AbstractPersistable<Long> {...}```. And the new interface GuestRepository should extend the JpaRepository interface: ```public interface GuestRepository extends JpaRepository<Guest, Long> {...}```. Now we could create a guestRepository in the controller with an annotation: ```@Autowired private GuestRepository guestRepository;```. JPA repository has already some functions like ```findAll()``` but we could create a custom query in the interface like this: 
```
@Query("select g from Guest g where g.username = :name") 
Guest findByUsername(@Param("name") String name);
```


## FLAW 3: Cross-Site Scripting (XSS)

**Description***: When a user signs up, a new column for table Guest is created that consists of username, name, phone number and email address. The input from the form fields is not validated in any way and this allows an attacker to make a stored/persistent XXS. The attacker can write malicious scripts or links into Guest values that then are stored and later on used.

**How to fix**: There a many ways to be protected against cross-site scripting attacks because there are many types of XSS attacks. An option for validating inputs is to use Hibernate Validator. The needed dependency for this is:  
```
<dependency>
    <groupId> org.jsoup </groupId>
    <artifactId> jsoup </artifactId>
    <version> 1.12.1 </version>
</dependency>
```  
And import is: ```org.hibernate.validator.constraints.SafeHtml```. Now inputs can be protected with annotation ```@Valid``` and object fields with ```@SafeHtml```. So for example in the Guest class every field would be like:  
```
@Entity
public class Guest extends AbstractPersistable<Long> {

    @SafeHtml
    private String username;
    @SafeHtml
    private String name;
    
    ...
}    
```  
And for the submitForm function in SignupController, though ```@Valid``` cannot be used ```@RequestParam```, we could bind our parameters into the Guest object so we would have the following:  
```
@Transactional
    @RequestMapping(value = "/form", method = RequestMethod.POST) 
    public String submitForm(@Valid @RequestBody Guest guest, @RequestParam String password, RedirectAttributes ra) {...}
```  
Or we could use ```@Validated``` annotation on the controller:  
```
@Controller
@Validated
public class SignupController {...}  
```
(That is at least what I grasped from these sources.
[1](https://sadique.io/blog/2015/12/05/validating-requestparams-and-pathvariables-in-spring-mvc/), 
[2](https://stackoverflow.com/questions/6203740/spring-web-mvc-validate-individual-request-params), 
[3](https://mvnrepository.com/artifact/org.jsoup/jsoup), 
[4](https://stackoverflow.com/questions/2147958/how-do-i-prevent-people-from-doing-xss-in-spring-mvc))


## FLAW 4: Broken access control

**Description**: Being able to see all registered guests and to delete guests (supposedly) requires andmin username and password. However a presisten attacker might find that like the path */username/show* shows the guest's (whose username is username) information, the path */admin/show* actually shows what only admins are supposed to see. Therefore just being able to navigate to this page gives an attacker the admin rights which are to be able to delete guest and to see all guests' information.

**How to fix**: A proper fix is, again, to have actual security configuration for logging in and out with different roles (guest / admin) and to use session ids. The easier and more 'home made' way would be to ask for the admin credentials every time the page */admin/show* is called. This is easily done with a get method that returns a form, and a post method that checks the password and only then returns the query results in a model to a page that is show without redirect, so the url would not change.


## FLAW 5: Insufficient Logging & Monitoring

*Description*: The application doesn't have a log or any runtime logging. If it did, and for example logged every deleted column, the admins would have noticed their broken access control flaw if someone other than an admin would've deleted guests.

*How to fix*: To log every sql query with values, we can add a logging dependency in the ```pom.xml``` file:  
```
<dependency>
    <groupId> org.springframework.boot </groupId>
    <artifactId> spring-boot-starter-web </artifactId>
</dependency>
```  
Then we'd need to create a ```application.properties``` file in *scr/main/resources* and add the following there:  
```
logging.level.org.hibernate.SQL=debug
logging.level.org.hibernate.type.descriptor.sql=trace
```  
(sources:
[5](https://docs.spring.io/spring-boot/docs/1.5.21.RELEASE/reference/html/howto-logging.html),
[6](https://springframework.guru/hibernate-show-sql/),
[7](https://stackoverflow.com/questions/30118683/how-to-log-sql-statements-in-spring-boot))

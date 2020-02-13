Hello,

About the application :

this is a DEMO application for 2 factors authentication (2FA with OTP SMS). its not recommended in the production neither to store the status bytes in a database, and it could be more optimized, but it fits the purpose of this Proof of concept.

How to make it work :

the application will start in 8081 port.
To Authenticated using Radius as Provider you will need a Radius Server well configured  and supports 2FA.this application is considered a RADIUS Client .

To test the application you need to send a POST request to http://<your_host>:8081/otp that contains in her body the following informations in a Json format :
    {
        "username" : "<your_username>",
        "password" : "<your_password>"
    }
if your credentials are correct you will see the folowing message : "One-Time password has been sent to mobile phone. Enter it to login".
then send the same request just changing your password by the OTP received as this way :
    {
        "username" : "<your_username>",
        "password" : "<your_OTP>"
    }
if your credentials are correct you will see the folowing message : "You have ben authenticated succefully M. user :<your_username>".
Now a session is created you can verify that by making a GET request to http://<your_host>:8081/name.

Process explanation

![alt text](https://github.com/zelkotb/RADIUS_POC/tree/master/src/main/resources/Radius_Spring.png)


# 🔒 TOTP Authcode
![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=for-the-badge&logo=spring&logoColor=white)
![H2 Database](https://img.shields.io/badge/H2-003545?style=for-the-badge&logo=h2&logoColor=white)
---

## 🚀 Overview

**AuthCode Application** is a Spring Boot-based application that implements Time-based One-Time Password (TOTP) for user authentication. Each 6-digit OTP is valid for 60 seconds and can only be used once. 

### Key Features:
- ✅ **60-Second Validity:** OTPs expire after 60 seconds.
- ✅ **Single-Use OTP:** OTPs are valid for only one authentication attempt.
- ✅ **Account Locking:** User account locks after 3 consecutive failed OTP validation attempts.
- ✅ **Unlock Service:** Locked users can reactivate their accounts by using the unlock service.
- ✅ **Offline Scaling:** Designed to scale offline.

---

## 🌟 Features

- **Time-Based OTP:** A secure 6-digit OTP is generated every 60 seconds.
- **Single Use:** An OTP can only be used once per validation.
- **Account Locking:** Accounts are locked after three invalid OTP validation attempts.
- **Unlock Service:** Locked accounts can be unlocked using the Unlock API.
- **Input Validation:** Strong input validation using regex and other checks to ensure data integrity.

---

## 🔧 API Endpoints

### 1. **Activate / Reactivate User**
Registers or reactivates a user to enable the TOTP service. Returns a unique secret that will be used to generate the OTP.

- **Method:** `POST`
- **URL:** `localhost:9002/app/activateOtp`
  
#### Request Body:
```json
{
  "userID": "252525",
  "password": "yourPassword"
}
```
#### Responses:  

##### Success Response:
```json
{
  "userID": "252525",
  "secret": "A2XSPRAVYAQ8V7EA",
  "message": "User registration successful."
}
```

##### Failure Responses:
  - Invalid userId:
      ````json
    {
      "userID": "2525525",
      "message": "Failed to generate secret, invalid userId.",
      "status": "Failed"
    }
    ````
- Reactivation failure due to incorrect credentials:

    ````json
  {
    "userID": "252525",
    "message": "Incorrect userId or password.",
    "status": "Failed"
  }
    ````
- Locked user tries to activate/reactivate:

    ````json
  {
    "userID": "242424",
    "message": "User's account is locked. Please unlock then reactivate your account.",
    "status": "Failed"
  }
    ````

### 2. **Generate OTP**
Generates a time-based 6-digit OTP for registered users. Requires the secret from the activate call.

- **Method:** `POST`
- **URL:** `localhost:9002/app/generateOtp`

#### Request Body:
```json
{
  "userID": "252525",
  "secret": "BZ2I50WVR5S57ZW5"
}
```
#### Responses:  

##### Success Response:
```json
{
  "message": "Success",
  "otp": "265094",
  "userID": "252525"
}
```
##### Failure Responses:
  - User has not activated the OTP service:
      ````json
    {
      "userID": "252525",
      "message": "AuthCode Generation failed, please activate / reactivate.",
      "status": "Failed"
    }
    ````

  - Invalid secret provided:
      ````json
    {
      "userID": "252525",
      "message": "Invalid secret, the length of the secret should be 16 without blank spaces.",
      "status": "Failed"
    }
    ````


### 3. **Validate OTP**
Validates the provided OTP for a registered user. If the OTP has been reused or there are invalid attempts, proper failure messages will be sent.

- **Method:** `POST`
- **URL:** `localhost:9002/web/validateOtp`

#### Request Body:
```json
{
  "userID": "252525",
  "otp": "739436"
}
```
#### Responses:  

##### Success Response:
```json
{
  "userID": "252525",
  "message": "Validation successful.",
  "authenticated": true
}
```

##### Failure Responses:
  - User not registered:
      ````json
    {
      "userID": "252525",
      "message": "AuthCode validation failed, user not registered.",
      "status": "Failed"
    }
    ````
      
  - OTP reused (first attempt)
      ````json
    {
      "userID": "252525",
      "message": "Validation failed - AuthCode reused, try with a new one.",
      "authenticated": false
    }
    ````

  - OTP reused (second attempt):
      ````json
    {
      "userID": "252525",
      "message": "Validation failed - AuthCode reused, try with a new one. Second invalid attempt, third invalid attempt will lock the account.",
      "authenticated": false
    }
    ````

  - Three consecutive invalid attempts (account locked):
    
     ````json
    {
        "userID": "252525",
        "message": "Validation failed - incorrect AuthCode. User's account is locked. Please unlock then reactivate your account.",
        "authenticated": false
     }
     ````


### 4. **Unlock User Account**
Unlocks a locked user account. The account must be unlocked before the user can reactivate OTP services.

- **Method:** `POST`
- **URL:** `localhost:9002/app/unlock`

#### Request Body:
```json
{
  "userId": "252525",
  "uuid": "4011206c-9ca2-4f3c-88e3-75db01bf08ae"
}
```

##### Success Response:
```json
{
  "status": true,
  "message": "User's account unlocked successfully."
}
```

#### Failure Responses:
```json
{
  "status": false,
  "message": "User's account unlock request failed."
}
```

---

## 🔒 Security Features

- **Account Locking:** Accounts are locked after three consecutive failed OTP validation attempts, preventing brute force attacks.
- **OTP Single Use:** Once an OTP is used for validation, it cannot be reused.
- **Input Validation:** Regex-based input validation to ensure security and integrity of request parameters.
- **Unlock Service:** Locked accounts can be unlocked using a secure service that requires a unique UUID.

---

## 🛠️ Technologies Used

- ![Spring Boot](https://img.shields.io/badge/SpringBoot-2.6.3-brightgreen.svg) **Spring Boot** - Application framework for Java.
- ![H2 Database](https://img.shields.io/badge/H2--Database-1.4.200-blue.svg) **H2 Database**
- ![Spring Data JPA](https://img.shields.io/badge/Spring--Data--JPA-2.6.3-yellow.svg) **Spring Data JPA** - For data persistence and ORM mapping.
- ![Java UUID](https://img.shields.io/badge/Java--UUID-unique-blue.svg) **Java UUID** - Used for generating unique IDs.

---

## 👤 Author

- **Name:** Arpit Srivastava
- **GitHub:** [git-arpit](https://github.com/git-arpit)
- **LinkedIn:** [Arpit Srivastava](https://www.linkedin.com/in/onarpit)

Feel free to reach out!

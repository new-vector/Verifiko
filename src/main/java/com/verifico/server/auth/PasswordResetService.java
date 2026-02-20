package com.verifico.server.auth;

// overall flow outline:
// user clicks forgot pass in frontend
// user enters email... if email found
// generate unique token with like a 15-30 min expiry
// send reset link to user email with the token in link
// get user to answer 2-3 security questions they setup when
// creating their account
// then prompt them to change password
// reference article:
// https://medium.com/@AlexanderObregon/making-a-basic-password-reset-workflow-in-spring-boot-b845bb4f7cf9

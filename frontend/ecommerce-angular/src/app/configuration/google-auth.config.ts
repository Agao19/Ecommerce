export const GOOGLE_AUTH_CONFIG = {
    clientId: '50781803266-3l5qghg0rdsa5l93pe009s3homaihepc.apps.googleusercontent.com',
    redirectUri: 'http://localhost:4200/authenticate',
    authUri: 'https://accounts.google.com/o/oauth2/auth',
    scope: 'openid profile email',
    responseType: 'code',  // Using implicit flow for frontend-only testing
    prompt: 'select_account'
};
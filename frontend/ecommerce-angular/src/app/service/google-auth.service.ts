import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { GOOGLE_AUTH_CONFIG } from '../configuration/google-auth.config';

@Injectable({
    providedIn: 'root'
})
export class GoogleAuthService {

    constructor(private router: Router) { }

    /**
     * Initiates Google OAuth login by redirecting to Google's authorization endpoint
     */
    initiateGoogleLogin(): void {
        const urlToGoogle = this.buildAuthUrl();
        window.location.href = urlToGoogle;
        // TODO: Thêm state sau
    }

    /**
     * Builds the Google OAuth authorization URL
     */
    private buildAuthUrl(): string {
        const params = new URLSearchParams({
            client_id: GOOGLE_AUTH_CONFIG.clientId,
            redirect_uri: GOOGLE_AUTH_CONFIG.redirectUri,
            response_type: GOOGLE_AUTH_CONFIG.responseType,  // 'code'
            scope: GOOGLE_AUTH_CONFIG.scope,
            prompt: GOOGLE_AUTH_CONFIG.prompt,
            // state: state,  // TODO: Thêm sau khi test code OK
        });

        return `${GOOGLE_AUTH_CONFIG.authUri}?${params.toString()}`;
    }

    handleCallback(): { code: string } | null {
        // ĐỔI: Đọc từ QUERY STRING thay vì HASH
        const params = new URLSearchParams(window.location.search);

        const code = params.get('code');
        const error = params.get('error');

        console.log('📦 Callback params:', { code, error });

        // Handle error from Google
        if (error) {
            console.error('OAuth error:', error, params.get('error_description'));
            return null;
        }

        if (!code) {
            console.error('Missing code in callback');
            return null;
        }

        // TODO: Validate state sau

        return { code };  // 
    }


    /**
     * Get stored user info
     */
    getUserInfo(): any {
        const userInfoStr = localStorage.getItem('user_info');
        return userInfoStr ? JSON.parse(userInfoStr) : null;
    }

    /**
     * Check if user is authenticated
     */
    isAuthenticated(): boolean {
        return !!localStorage.getItem('token');  // Token từ backend
    }

    /**
     * Logout - clear tokens
     */
    logout(): void {
        localStorage.removeItem('token');
        localStorage.removeItem('user_info');
        localStorage.removeItem('role');
        this.router.navigate(['/login']);
    }
}
import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { GoogleAuthService } from '../service/google-auth.service';
import { ApiService } from '../service/api.service';
import { CommonModule } from '@angular/common';

@Component({
    selector: 'app-authenticate',
    standalone: true,
    imports: [CommonModule],
    templateUrl: './authenticate.component.html',
    styleUrls: ['./authenticate.component.css']
})
export class AuthenticateComponent implements OnInit {

    message: string = 'Authenticating with Google...';
    isError: boolean = false;
    isLoading: boolean = true;

    constructor(
        private googleAuthService: GoogleAuthService,
        private apiService: ApiService,
        private router: Router
    ) { }

    ngOnInit(): void {
        console.log('Current URL:', window.location.href);
        this.handleOAuthCallback();
    }

    private handleOAuthCallback(): void {
        // ① Parse CODE từ URL
        const result = this.googleAuthService.handleCallback();

        if (!result) {
            this.isError = true;
            this.isLoading = false;
            this.message = 'Authentication failed. Invalid callback.';
            setTimeout(() => this.router.navigate(['/login']), 3000);
            return;
        }

        console.log('✅ Received code:', result.code);

        this.apiService.exchangeGoogleCode(result.code).subscribe({
            next: (response) => {
                console.log('✅ Backend response:', response);

                if (response.token) {
                    localStorage.setItem('token', response.token);
                    localStorage.setItem('role', response.role || 'USER');

                    // Emit auth status changed
                    this.apiService.authStatuschanged.emit();

                    this.isLoading = false;
                    this.message = 'Login successful! Redirecting...';

                    // Navigate to profile
                    setTimeout(() => {
                        this.router.navigate(['/profile']);
                    }, 1500);
                } else {
                    throw new Error('No token received from backend');
                }
            },
            error: (err) => {
                console.error('❌ Backend error:', err);

                this.isError = true;
                this.isLoading = false;
                this.message = err.error?.message || 'Failed to authenticate with backend.';

                setTimeout(() => {
                    this.router.navigate(['/login']);
                }, 3000);
            }
        });
    }
}
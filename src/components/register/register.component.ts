import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '../../app/services/auth.service';
import { CommonModule } from '@angular/common';

@Component({
  standalone: true, // ✅ Required for standalone + loadComponent
  selector: 'app-register',
  templateUrl: './register.component.html',
  imports: [CommonModule, ReactiveFormsModule]
})
export class RegisterComponent {
  registerForm: FormGroup;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.registerForm = this.fb.group({
      username: ['', Validators.required],
      password: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      fullName: ['', Validators.required],
      profileImage: [''],
      location: [''],
      userType: ['PROVIDER', Validators.required],
      // Provider fields
      yearsOfExperience: [''],
      bio: [''],
      languages: [''],
      // Seeker fields
      preferredContactMethod: [''],
      notificationPreferences: ['']
    });
  }

  onSubmit() {
    if (this.registerForm.valid) {
      const formValue = { ...this.registerForm.value };

      // Strip out irrelevant fields based on user type
      if (formValue.userType === 'PROVIDER') {
        delete formValue.preferredContactMethod;
        delete formValue.notificationPreferences;
      } else if (formValue.userType === 'SEEKER') {
        delete formValue.yearsOfExperience;
        delete formValue.bio;
        delete formValue.languages;
      }

      this.auth.register(formValue).subscribe({
        next: () => this.router.navigate(['/login']),
        error: () => this.error = 'Registration failed'
      });
    }
  }
}

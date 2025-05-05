// // import { Component } from '@angular/core';
// // import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule, Validators } from '@angular/forms';

// // import { Router, RouterLink } from '@angular/router';
// // import { AuthService } from '../../app/services/auth.service';
// // import { CommonModule } from '@angular/common';

// // @Component({
// //   selector: 'app-login',
// //   templateUrl: './login.component.html',
// //   imports: [FormsModule,ReactiveFormsModule, CommonModule],
// // })
// // export class LoginComponent {
// //   loginForm: FormGroup;
// //   error: string | null = null;

// //   constructor(
// //     private fb: FormBuilder,
// //     private auth: AuthService,
// //     private router: Router
// //   ) {
// //     this.loginForm = this.fb.group({
// //       username: ['', Validators.required],
// //       password: ['', Validators.required]
// //     });
// //   }

// //   onSubmit() {
// //     if (this.loginForm.valid) {
// //       this.auth.login(this.loginForm.value).subscribe({
// //         next: () => this.router.navigate(['/']),
// //         error: () => this.error = 'Invalid credentials'
// //       });
// //     }
// //   }
// // }

// import { Component } from '@angular/core';
// import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
// import { Router } from '@angular/router';
// import { AuthService } from '../../app/services/auth.service';

// @Component({
//   selector: 'app-login',
//   templateUrl: './login.component.html',
//   imports:[ReactiveFormsModule]
// })
// export class LoginComponent {
//   loginForm: FormGroup;
//   error: string | null = null;

//   constructor(
//     private fb: FormBuilder,
//     private auth: AuthService,
//     private router: Router
//   ) {
//     this.loginForm = this.fb.group({
//       username: ['', Validators.required],
//       password: ['', Validators.required]
//     });
//   }

//   onSubmit() {
//     if (this.loginForm.valid) {
//       this.auth.login(this.loginForm.value).subscribe({
//         next: () => this.router.navigate(['/']),
//         error: () => this.error = 'Invalid credentials'
//       });
//     }
//   }
// }

import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { AuthService } from '../../app/services/auth.service';

@Component({
  standalone: true,
  selector: 'app-login',
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
})
export class LoginComponent {
  loginForm: FormGroup;
  error: string | null = null;

  constructor(
    private fb: FormBuilder,
    private auth: AuthService,
    private router: Router
  ) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required, Validators.email]], // ✅ email
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    const { email, password } = this.loginForm.value;
    this.auth.login(email, password).subscribe({
      next: res => {
        this.router.navigate(['/']);
      },
      error: () => this.error = 'Invalid credentials'
    });
  }
}


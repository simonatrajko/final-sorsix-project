import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup ,ReactiveFormsModule,Validators} from '@angular/forms';
import { Router } from '@angular/router';
import {SignUpService} from '../../services/sign-up.service';
@Component({
  selector: 'app-sign-up',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  signUpForm: FormGroup;

  constructor(private fb: FormBuilder,private router:Router,private signService:SignUpService) {
    this.signUpForm = this.fb.group({
      fullName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      profileImage: [null],
      location: [''],
      role: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      let data = this.signUpForm.value
      this.router.navigateByUrl("/")
      this.signService.handleSignUp(data)
    }
  }
}

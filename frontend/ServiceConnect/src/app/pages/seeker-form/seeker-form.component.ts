import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';

@Component({
  selector: 'app-seeker-form',
  imports: [ReactiveFormsModule],
  templateUrl: './seeker-form.component.html',
  styleUrl: './seeker-form.component.css'
})
export class SeekerFormComponent {

  signUpForm: FormGroup;
  
  constructor(private fb: FormBuilder,private router:Router,private userService:UserService) {
    this.signUpForm = this.fb.group({
      fullName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      profileImage: [null],
      location: [''],
    });
    
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      let data = this.signUpForm.value
      data.role="seeker"
      this.userService.handleSignUp(data)      
      this.router.navigateByUrl("/")
    }
  }
}

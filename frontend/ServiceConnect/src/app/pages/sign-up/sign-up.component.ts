import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup ,ReactiveFormsModule,Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import {SignUpService} from '../../services/sign-up.service';
import { LoginService } from '../../services/login.service';
@Component({
  selector: 'app-sign-up',
  imports: [CommonModule,ReactiveFormsModule],
  templateUrl: './sign-up.component.html',
  styleUrl: './sign-up.component.css'
})
export class SignUpComponent {
  signUpForm: FormGroup;
  mode:string="sign up"
  username:string|null=""
  constructor(private fb: FormBuilder,private router:Router,private signService:SignUpService,private currentRoute:ActivatedRoute,private currentUserService:LoginService) {
    this.signUpForm = this.fb.group({
      fullName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      profileImage: [null],
      location: [''],
      role: ['', Validators.required]
    });
    this.username=this.currentRoute.snapshot.paramMap.get('username')
    if(this.username){
      this.mode="edit"
    }
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      let data = this.signUpForm.value
      if(this.mode=="edit"){
          this.currentUserService.handleEdit(data)
          this.router.navigateByUrl(`user/${this.username}`)
      }
      else{
        this.signService.handleSignUp(data)
        this.router.navigateByUrl("/") 
      }
    }
  }
}

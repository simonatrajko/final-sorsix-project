import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserService } from '../../services/user.service';
import { AuthService } from '../../services/auth-service.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;

  constructor(private fb: FormBuilder,private router:Router,  private currentUserService: UserService,private authService:AuthService) {
    this.loginForm = this.fb.group({
      email: ['', [Validators.required,Validators.email]],
      password: ['', Validators.required]
    });
  }

  onSubmit() {
    if (this.loginForm.valid) {
      const data = this.loginForm.value;
      console.log(data)
      this.authService.login(data.email,data.password).subscribe(res=>{
        const user=res.user
        const tokens= res.tokens
        if(user){ 
          this.authService.storeTokens(tokens)
          localStorage.setItem("currentUser",JSON.stringify(user))
          this.currentUserService.setCurrentUser(user)
          this.router.navigateByUrl(`user/${user.username}`)
        }
      })
    }
  }
}

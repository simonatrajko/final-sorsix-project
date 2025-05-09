import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';
import { AuthService } from '../../services/auth-service.service';

@Component({
  selector: 'app-provider-from',
  imports: [ReactiveFormsModule,ScheduleSlotFormComponent],
  templateUrl: './provider-from.component.html',
  styleUrl: './provider-from.component.css'
})
export class ProviderFromComponent {

  signUpForm: FormGroup;
  
  constructor(private fb: FormBuilder,private router:Router,private auth:AuthService) {
    this.signUpForm = this.fb.group({
      fullName: ['', Validators.required],
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      profileImage: [''],
      location: [''],
    });
    
  }

  onSubmit() {
    if (this.signUpForm.valid) {
      let data = this.signUpForm.value
      data.userType="PROVIDER"
      data.preferredContactMethod=""
      data.preferredContactMethod=""    
      this.auth.register(data).subscribe(res=>console.log("ova go vrati backendot pri register" +res))
      this.router.navigateByUrl("/")
      }
    }
}

  


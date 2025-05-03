import { Component } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '../../services/user.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';

@Component({
  selector: 'app-provider-from',
  imports: [ReactiveFormsModule,ScheduleSlotFormComponent],
  templateUrl: './provider-from.component.html',
  styleUrl: './provider-from.component.css'
})
export class ProviderFromComponent {

  signUpForm: FormGroup;
  initialScheduleSlots:ScheduleSlot[]=[]
  i=0
  slots=[{j:this.i}]
  // ???????
  constructor(private fb: FormBuilder,private router:Router,private userService:UserService,private scheduleSlotsService:ScheduleSlotsService) {
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
      data.role="provider"
      if(this.initialScheduleSlots.length==0){
        alert("please add at least one schedule slot")

      }
      else{
        this.userService.handleSignUp(data)
        this.initialScheduleSlots.forEach(s=>s.providerUsername=data.username)
        this.scheduleSlotsService.handleInitialScheduleSlots(this.initialScheduleSlots,data.username)
        this.router.navigateByUrl("/")

      }
    }
  }

  addSlotForm() {
    this.i++
    this.slots.push({j:this.i});
  }

  handleSlotCreated(slot:ScheduleSlot){
    slot.id=this.i
    this.initialScheduleSlots.push(slot)
    console.log(this.initialScheduleSlots)
  }
}

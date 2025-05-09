import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlotCardComponent } from '../../components/schedule-slot-card/schedule-slot-card.component';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';
ScheduleSlotFormComponent
@Component({
  selector: 'app-schedule-provider',
  imports: [ScheduleSlotCardComponent,ScheduleSlotFormComponent],
  templateUrl: './schedule-provider.component.html',
  styleUrl: './schedule-provider.component.css'
})
export class ScheduleProviderComponent {
  showForm=false
  username:string|null
  constructor(private route:ActivatedRoute){
    this.username= this.route.snapshot.paramMap.get('username');
   
  }
  ngOnInit(){
    
  }

  addSlotForm(){
    this.showForm=true
  }

  removeForm(t:boolean){
    console.log("This gets called")
    console.log(t)
    this.showForm=t
  }
  
}

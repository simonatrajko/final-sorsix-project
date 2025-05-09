import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ScheduleSlotCardComponent } from '../../components/schedule-slot-card/schedule-slot-card.component';
import { ScheduleSlotFormComponent } from '../../components/schedule-slot-form/schedule-slot-form.component';
import { ScheduleSlotsService } from '../../services/schedule-slots.service';
import { ScheduleSlot } from '../../models/ScheduleSlot';
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
  slots:ScheduleSlot[]=[]
  constructor(private route:ActivatedRoute,private scheduleSlotService:ScheduleSlotsService){
    this.username= this.route.snapshot.paramMap.get('username');
   
  }
  ngOnInit(){
    this.scheduleSlotService.getAllSlotsByProvider().subscribe(res=>{
      this.slots=res
    })
  }

  handleDeleted(id:number){
    this.slots=this.slots.filter(s=>s.id!=id)
  }

  addSlotForm(){
    this.showForm=true
  }

  removeForm(t:boolean){
    this.showForm=t
  }
  
}
